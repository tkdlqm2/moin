package com.moin.transfer.service.transfer;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.dto.request.QuoteRegisterRequest;
import com.moin.transfer.dto.request.QuoteRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.QuoteApiResponse;
import com.moin.transfer.dto.response.QuoteListData;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import com.moin.transfer.repository.QuoteRepository;
import com.moin.transfer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
    private final ExchangeRateService exchangeRateService;
    private final FeeCalculator feeCalculator;
    private final QuoteValidator quoteValidator;
    private final QuoteRepository quoteRepository;
    private final UserService userService;

    @Override
    @Transactional
    public QuoteApiResponse createQuote(QuoteRequest request) {
        User user = userService.getCurrentUser();

        quoteValidator.validateAmount(request.getAmount());

        Quote quote = createQuoteEntity(request, user);
        Quote savedQuote = quoteRepository.save(quote);

        return QuoteApiResponse.success(QuoteConverter.toQuoteData(savedQuote));
    }

    @Override
    @Transactional
    public ApiResponse<?> requestQuote(QuoteRegisterRequest request) {
        User user = userService.getCurrentUser();
        Quote quote = quoteValidator.validateAndGetQuote(request.getQuoteId());

        quoteValidator.validateDailyLimit(user, quote.getTargetAmount(), quote.getTargetCurrency(), quote.getAmount());

        quote.markAsUsed();
        quoteRepository.save(quote);

        return ApiResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public QuoteListData quoteList(String authorizationHeader) {
        User user = userService.getCurrentUser();
        List<Quote> todayQuotes = getTodayQuotes(user);
        List<Quote> allQuotes = getAllQuotes(user);
        BigDecimal usdExchangeRate = exchangeRateService.getExchangeRate(Currency.USD);

        return buildQuoteListResponse(user, todayQuotes, allQuotes, usdExchangeRate);
    }

    private Quote createQuoteEntity(QuoteRequest request, User user) {
        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(request.getTargetCurrency());
        BigDecimal fee = feeCalculator.calculateFee(request.getAmount(), request.getTargetCurrency());
        BigDecimal targetAmount = calculateTargetAmount(request, exchangeRate, fee);

        quoteValidator.validateTargetAmount(targetAmount);

        return Quote.builder()
                .user(user)
                .currency(request.getTargetCurrency())
                .amount(BigDecimal.valueOf(request.getAmount()))
                .targetCurrency(request.getTargetCurrency())
                .exchangeRate(exchangeRate)
                .fee(fee)
                .targetAmount(targetAmount)
                .expireTime(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();
    }

    private BigDecimal calculateTargetAmount(
            QuoteRequest request,
            BigDecimal exchangeRate,
            BigDecimal fee
    ) {
        BigDecimal sendingAmount = BigDecimal.valueOf(request.getAmount()).subtract(fee);
        return sendingAmount.divide(
                exchangeRate,
                request.getTargetCurrency().getDefaultFractionDigits(),
                RoundingMode.HALF_UP
        );
    }

    private List<Quote> getTodayQuotes(User user) {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return quoteRepository.findAllByUserAndUsedTrueAndCreationDtmBetween(
                user, today, today.plusDays(1));
    }

    private List<Quote> getAllQuotes(User user) {
        return quoteRepository.findAllByUserAndUsedTrueOrderByCreationDtmDesc(user);
    }

    private QuoteListData buildQuoteListResponse(
            User user,
            List<Quote> todayQuotes,
            List<Quote> allQuotes,
            BigDecimal usdExchangeRate
    ) {
        return QuoteListData.builder()
                .userId(user.getEmail())
                .name(user.getName())
                .todayTransferCount(todayQuotes.size())
                .todayTransferUsdAmount(calculateTotalUsdAmount(todayQuotes, usdExchangeRate))
                .history(QuoteConverter.toTransferHistoryList(allQuotes, usdExchangeRate))
                .build();
    }

    private BigDecimal calculateTotalUsdAmount(List<Quote> quotes, BigDecimal usdExchangeRate) {
        return quotes.stream()
                .map(quote -> {
                    // 금액에서 수수료를 뺀 후 반올림
                    BigDecimal sourceAmountInKrw = quote.getAmount()
                            .subtract(quote.getFee())
                            .setScale(0, RoundingMode.HALF_UP);

                    if (quote.getTargetCurrency() == Currency.USD) {
                        return quote.getTargetAmount()
                                .setScale(Currency.USD.getDefaultFractionDigits(), RoundingMode.HALF_UP);
                    }

                    int krwScale = Currency.KRW.getDefaultFractionDigits();

                    // 환율 정보 반올림
                    BigDecimal exchangeRate = usdExchangeRate.setScale(12, RoundingMode.HALF_UP);

                    // 나눗셈 후 반올림
                    return sourceAmountInKrw
                            .divide(exchangeRate, krwScale, RoundingMode.HALF_UP)
                            .setScale(krwScale, RoundingMode.HALF_UP);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                // 최종 합계 반올림
                .setScale(Currency.USD.getDefaultFractionDigits(), RoundingMode.HALF_UP);
    }
}


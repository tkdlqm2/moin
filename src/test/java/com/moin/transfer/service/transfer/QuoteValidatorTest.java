package com.moin.transfer.service.transfer;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.common.enums.CurrencyCode;
import com.moin.transfer.common.external.client.upbit.client.UpbitApiClient;
import com.moin.transfer.dto.response.QuoteAmount;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import com.moin.transfer.repository.QuoteRepository;
import com.moin.transfer.service.transfer.fixture.QuoteValidatorFixture;
import com.moin.transfer.service.transfer.fixture.UpbitResponseFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteValidatorTest {

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private UpbitApiClient upbitApiClient;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private QuoteValidator quoteValidator;

    @BeforeEach
    void setUp() {
        exchangeRateService = new ExchangeRateService(upbitApiClient);
        quoteValidator = new QuoteValidator(quoteRepository, exchangeRateService);
    }

    @Test
    @DisplayName("음수 금액 검증시 예외 발생")
    void validNegativeAmountFail() {
        // given
        long amount = -1000;

        // when
        QuoteException exception = assertThrows(QuoteException.class,
                () -> quoteValidator.validateAmount(amount));

        // then
        assertEquals(QuoteErrorCode.NEGATIVE_NUMBER, exception.getCommonErrorCodeType());
    }

    @Test
    @DisplayName("음수 목표 금액 검증시 예외 발생")
    void validNegativeTargetAmountFail() {
        // given
        BigDecimal targetAmount = new BigDecimal("-1000");

        // when
        QuoteException exception = assertThrows(QuoteException.class,
                () -> quoteValidator.validateTargetAmount(targetAmount));

        // then
        assertEquals(QuoteErrorCode.NEGATIVE_NUMBER, exception.getCommonErrorCodeType());
    }

    @Test
    @DisplayName("개인 사용자의 JPY 송금 한도 초과시 예외 발생")
    void throwExceptionWhenIndividualUserExceedsJpyLimit() {
        // given
        User user = QuoteValidatorFixture.createIndividualUser(1L);
        BigDecimal targetAmount = new BigDecimal("500000");  // 50만엔
        BigDecimal amount = new BigDecimal("5000000");       // 500만원

        List<QuoteAmount> existingQuotes = List.of(
                new QuoteAmount(
                        new BigDecimal("5000000"),  // 기존 500만원
                        Currency.JPY,
                        new BigDecimal("500000")    // 기존 50만엔
                )
        );

        // UpbitApiClient mock 설정
        when(upbitApiClient.getExchangeRate(CurrencyCode.KRW_USD))
                .thenReturn(Mono.just(List.of(UpbitResponseFixture.create(
                        CurrencyCode.KRW_USD,
                        new BigDecimal("1300")
                ))));

        when(quoteRepository.findDailyQuoteAmountsByUser(
                eq(user.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(existingQuotes);

        // when & then
        assertThrows(QuoteException.class, () ->
                quoteValidator.validateDailyLimit(user, targetAmount, Currency.JPY, amount)
        );
    }


    @Test
    @DisplayName("만료된 견적 조회시 예외 발생")
    void validateExpiredQuoteFail() {
        // given
        long quoteId = 1L;
        Quote expiredQuote = QuoteValidatorFixture.createExpiredQuote(quoteId);

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(expiredQuote));

        // when
        QuoteException exception = assertThrows(QuoteException.class,
                () -> quoteValidator.validateAndGetQuote(quoteId));

        // then
        assertEquals(QuoteErrorCode.QUOTE_EXPIRED, exception.getCommonErrorCodeType());
        verify(quoteRepository).findById(quoteId);
    }

    @Test
    @DisplayName("이미 사용된 견적 조회시 예외 발생")
    void validateUsedQuoteFail() {
        // given
        long quoteId = 1L;
        Quote usedQuote = QuoteValidatorFixture.createUsedQuote(quoteId);

        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(usedQuote));

        // when
        QuoteException exception = assertThrows(QuoteException.class,
                () -> quoteValidator.validateAndGetQuote(quoteId));

        // then
        assertEquals(QuoteErrorCode.QUOTE_ALREADY_USED, exception.getCommonErrorCodeType());
        verify(quoteRepository).findById(quoteId);
    }
}
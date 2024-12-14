package com.moin.transfer.service.transfer;

import com.moin.transfer.common.enums.Currency;
import com.moin.transfer.dto.request.QuoteRegisterRequest;
import com.moin.transfer.dto.request.QuoteRequest;
import com.moin.transfer.dto.response.ApiResponse;
import com.moin.transfer.dto.response.QuoteApiResponse;
import com.moin.transfer.entity.Quote;
import com.moin.transfer.entity.User;
import com.moin.transfer.exception.quote.QuoteErrorCode;
import com.moin.transfer.exception.quote.QuoteException;
import com.moin.transfer.repository.QuoteRepository;
import com.moin.transfer.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {
    @Mock
    private ExchangeRateService exchangeRateService;
    @Mock
    private FeeCalculator feeCalculator;
    @Mock
    private QuoteValidator quoteValidator;
    @Mock
    private QuoteRepository quoteRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    @DisplayName("견적서 생성 성공")
    void createQuote_Success() {
        // given
        User user = User.builder()
                .email("test@test.com")
                .name("test")
                .build();

        QuoteRequest request = new QuoteRequest(10000L, Currency.USD);
        BigDecimal exchangeRate = new BigDecimal("1300.50");
        BigDecimal fee = new BigDecimal("3000");

        when(userService.getCurrentUser()).thenReturn(user);
        when(exchangeRateService.getExchangeRate(Currency.USD)).thenReturn(exchangeRate);
        when(feeCalculator.calculateFee(10000L, Currency.USD)).thenReturn(fee);
        when(quoteRepository.save(any(Quote.class))).thenAnswer(i -> i.getArgument(0));

        // when
        QuoteApiResponse response = transferService.createQuote(request);

        // then
        assertNotNull(response.getQuote());
        verify(quoteValidator).validateAmount(10000L);
        verify(quoteRepository).save(any(Quote.class));
    }

    @Test
    @DisplayName("음수 금액으로 견적서 생성 시 예외 발생")
    void createQuote_withNegativeAmount() {
        // given
        QuoteRequest request = new QuoteRequest(-1000L, Currency.USD);
        User user = User.builder().build();

        when(userService.getCurrentUser()).thenReturn(user);
        doThrow(new QuoteException(QuoteErrorCode.NEGATIVE_NUMBER))
                .when(quoteValidator).validateAmount(-1000L);

        // when
        QuoteException exception = assertThrows(QuoteException.class,
                () -> transferService.createQuote(request));

        // then
        assertEquals(QuoteErrorCode.NEGATIVE_NUMBER, exception.getCommonErrorCodeType());
    }

    @Test
    @DisplayName("견적서 등록 요청 성공")
    void requestQuote_Success() {
        // given
        Long quoteId = 1L;
        QuoteRegisterRequest request = new QuoteRegisterRequest(quoteId);
        User user = User.builder().build();
        Quote quote = Quote.builder()
                .targetAmount(new BigDecimal("1000"))
                .build();

        when(userService.getCurrentUser()).thenReturn(user);
        when(quoteValidator.validateAndGetQuote(quoteId)).thenReturn(quote);
        when(quoteRepository.save(any(Quote.class))).thenReturn(quote);

        // when
        ApiResponse<?> response = transferService.requestQuote(request);

        // then
        assertTrue(response.isSuccess());
        verify(quoteValidator).validateDailyLimit(user, quote.getTargetAmount(), quote.getCurrency(), quote.getAmount());
        verify(quoteRepository).save(quote);
    }

    @Test
    @DisplayName("일일 한도 초과로 견적서 등록 실패")
    void requestQuote_DailyLimitExceeded() {
        // given
        String authHeader = "Bearer token";
        Long quoteId = 1L;
        QuoteRegisterRequest request = new QuoteRegisterRequest(quoteId);
        User user = User.builder().build();
        Quote quote = Quote.builder()
                .targetAmount(new BigDecimal("10000000"))
                .build();

        when(userService.getCurrentUser()).thenReturn(user);
        when(quoteValidator.validateAndGetQuote(quoteId)).thenReturn(quote);
        doThrow(new QuoteException(QuoteErrorCode.LIMIT_EXCESS))
                .when(quoteValidator).validateDailyLimit(user, quote.getTargetAmount(), quote.getCurrency(), quote.getAmount());

        // when
        QuoteException exception = assertThrows(QuoteException.class,
                () -> transferService.requestQuote(request));

        // then
        assertEquals(QuoteErrorCode.LIMIT_EXCESS, exception.getCommonErrorCodeType());
    }
}
package com.moin.transfer.common.external.client;

import com.moin.transfer.common.enums.CurrencyCode;
import reactor.core.publisher.Mono;

public interface ExternalApiClient {
    <O> Mono<O> callApi(CurrencyCode... currencyCodes);
}
package com.polynomeer.app.api.price;

public interface ExchangeFilter {
    /**
     * @param symbolExchange 메타에 기록된 종목의 거래소 코드 (예: NASDAQ)
     * @param requested      요청 파라미터 exchange (null 또는 "US"/"NASDAQ"...)
     */
    boolean isAllowed(String symbolExchange, String requested);
}

package com.polynomeer.domain.popular.port;

import com.polynomeer.domain.popular.model.PopularWindow;

import java.time.Instant;
import java.util.List;

/**
 * 요청 수 기준 인기 종목 집계를 위한 도메인 포트.
 * 구현체는 인프라(예: Redis ZSET)를 사용하되, 도메인은 이 인터페이스만 의존한다.
 * <p>
 * 시간 기준은 UTC를 권장한다.
 */
public interface PopularCounterRepository {

    /**
     * 특정 윈도우 버킷에 대해 티커의 요청수를 1 증가시킨다.
     *
     * @param ticker 티커 (예: AAPL)
     * @param window 집계 윈도우 (예: H1)
     * @param atUtc  증가가 반영될 기준 시각(UTC)
     */
    void increment(String ticker, PopularWindow window, Instant atUtc);

    /**
     * 주어진 윈도우 기준 상위 N개의 티커와 요청수를 반환한다.
     * 구현체는 nowUtc를 기준으로 필요한 버킷들을 합산한다.
     *
     * @param window 집계 윈도우
     * @param limit  상위 개수 (서비스 레벨에서 최대 10으로 캡)
     * @param nowUtc 현재 기준 시각(UTC)
     * @return 랭킹 아이템 리스트(티커, 요청수)
     */
    List<RankItem> topN(PopularWindow window, int limit, Instant nowUtc);

    /**
     * 인기 랭킹 단일 항목 (도메인 전용 값 객체).
     */
    record RankItem(String ticker, long requests) {
    }
}

package com.polynomeer.infra.redis;

import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RedisPopularCounterAdapter implements PopularCounterPort {

    private final StringRedisTemplate redis;
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(ZoneOffset.UTC);

    @Override
    public void increment(String ticker, PopularWindow window, Instant atUtc) {
        String key = minuteKey(window, atUtc);
        redis.opsForZSet().incrementScore(key, ticker, 1.0);
        redis.expire(key, window.length().plusSeconds(3600)); // 여유 TTL
    }

    @Override
    public void incrementBatch(String ticker, Collection<PopularWindow> windows, Instant atUtc) {
        if (windows == null || windows.isEmpty()) return;
        // 파이프라인: 네트워크 RTT 1회
        redis.executePipelined(new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public Object execute(@NotNull RedisOperations operations) {
                RedisOperations<String, String> ops = (RedisOperations<String, String>) operations;
                for (PopularWindow w : windows) {
                    String key = minuteKey(w, atUtc);
                    ops.opsForZSet().incrementScore(key, ticker, 1.0);
                    ops.expire(key, Duration.ofSeconds(w.length().toSeconds() + 3600));
                }
                return null;
            }
        });
    }

    @Override
    public List<RankItem> topN(PopularWindow window, int limit, Instant nowUtc) {
        List<String> keys = minuteKeys(window, nowUtc);
        if (keys.isEmpty()) return List.of();

        String resultKey = "pop:rank:" + window.name().toLowerCase(Locale.ROOT);
        String base = keys.getFirst();
        List<String> others = keys.subList(1, keys.size());

        redis.opsForZSet().unionAndStore(base, others, resultKey);
        redis.expire(resultKey, Duration.ofSeconds(20));

        Set<ZSetOperations.TypedTuple<String>> tuples =
                redis.opsForZSet().reverseRangeWithScores(resultKey, 0, limit - 1);
        if (tuples == null) return List.of();
        List<RankItem> out = new ArrayList<>(tuples.size());
        for (var t : tuples) {
            out.add(new RankItem(t.getValue(), t.getScore() == null ? 0L : t.getScore().longValue()));
        }
        return out;
    }

    private String minuteKey(PopularWindow w, Instant i) {
        return "pop:req:" + w.name().toLowerCase(Locale.ROOT) + ":" + F.format(i.truncatedTo(ChronoUnit.MINUTES));
    }

    private List<String> minuteKeys(PopularWindow w, Instant now) {
        long m = w.length().toMinutes();
        List<String> keys = new ArrayList<>((int) m);
        Instant base = now.truncatedTo(ChronoUnit.MINUTES);
        for (int i = 0; i < m; i++) {
            keys.add(minuteKey(w, base.minus(i, ChronoUnit.MINUTES)));
        }
        return keys;
    }
}

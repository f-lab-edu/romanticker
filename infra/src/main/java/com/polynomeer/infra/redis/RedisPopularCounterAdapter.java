package com.polynomeer.infra.redis;

import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisPopularCounterAdapter implements PopularCounterPort {

    private final StringRedisTemplate redis;
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyyMMddHHmm").withZone(ZoneOffset.UTC);

    @Override
    public void increment(String ticker, PopularWindow window, Instant atUtc) {
        String key = minuteKey(window, atUtc);
        redis.opsForZSet().incrementScore(key, ticker, 1.0);
        redis.expire(key, window.length().plusSeconds(3600));
    }

    @Override
    public List<RankItem> topN(PopularWindow window, int limit, Instant nowUtc) {
        List<String> keys = minuteKeys(window, nowUtc);
        if (keys.isEmpty()) return List.of();

        String resultKey = "pop:rank:" + window.name().toLowerCase(Locale.ROOT);
        // union & short cache
        redis.execute((RedisCallback<Object>) conn -> {
            byte[][] arr = keys.stream().map(String::getBytes).toArray(byte[][]::new);
            conn.zUnionStore(resultKey.getBytes(), arr);
            conn.expire(resultKey.getBytes(), 20);
            return null;
        });

        Set<ZSetOperations.TypedTuple<String>> tuples =
                redis.opsForZSet().reverseRangeWithScores(resultKey, 0, limit - 1);

        if (tuples == null) return List.of();
        List<RankItem> out = new ArrayList<>();
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

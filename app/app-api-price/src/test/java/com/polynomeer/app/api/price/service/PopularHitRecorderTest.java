package com.polynomeer.app.api.price.service;

import com.polynomeer.app.api.price.config.PopularCountProperties;
import com.polynomeer.domain.popular.model.PopularWindow;
import com.polynomeer.domain.popular.port.PopularCounterPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PopularHitRecorderTest {

    @Test
    void recordHit_callsIncrementBatchOnce_withConfiguredWindows() {
        PopularCounterPort counter = mock(PopularCounterPort.class);

        PopularCountProperties props = new PopularCountProperties();
        props.setWindows(EnumSet.of(PopularWindow.M15, PopularWindow.H1));

        PopularHitRecorder recorder = new PopularHitRecorder(counter, props);

        Instant now = Instant.parse("2024-01-01T00:00:00Z");
        recorder.recordHit("AAPL", now);

        ArgumentCaptor<Set<PopularWindow>> setCaptor = ArgumentCaptor.forClass(Set.class);
        verify(counter, times(1)).increment(eq("AAPL"), setCaptor.capture(), eq(now));
        assertThat(setCaptor.getValue()).containsExactlyInAnyOrder(PopularWindow.M15, PopularWindow.H1);
        verifyNoMoreInteractions(counter);
    }
}

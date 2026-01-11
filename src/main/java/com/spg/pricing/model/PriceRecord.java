package com.spg.pricing.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public final class PriceRecord {

    private final String instrumentId;
    private final Instant asOf;
    private final Map<String, Object> payload;

    public PriceRecord(String instrumentId, Instant asOf, Map<String, Object> payload) {
        this.instrumentId = Objects.requireNonNull(instrumentId);
        this.asOf = Objects.requireNonNull(asOf);
        this.payload = Map.copyOf(payload);
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public Instant getAsOf() {
        return asOf;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}

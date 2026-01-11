package com.spg.pricing.store;

import com.spg.pricing.model.PriceRecord;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PriceStore {

    private final ConcurrentHashMap<String, PriceRecord> latestPrices = new ConcurrentHashMap<>();

    public void commit(PriceRecord record) {
        latestPrices.merge(
                record.getInstrumentId(),
                record,
                (existing, incoming) ->
                        incoming.getAsOf().isAfter(existing.getAsOf()) ? incoming : existing
        );
    }

    public Optional<PriceRecord> getLatest(String instrumentId) {
        return Optional.ofNullable(latestPrices.get(instrumentId));
    }
}

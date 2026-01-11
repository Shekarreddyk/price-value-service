package com.spg.pricing.service;



import com.spg.pricing.api.PriceService;
import com.spg.pricing.batch.BatchRun;
import com.spg.pricing.batch.BatchStatus;
import com.spg.pricing.model.PriceRecord;
import com.spg.pricing.store.PriceStore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPriceService implements PriceService {

    private final ConcurrentHashMap<String, BatchRun> batches = new ConcurrentHashMap<>();
    private final PriceStore store = new PriceStore();

    @Override
    public String startBatch() {
        String batchId = UUID.randomUUID().toString();
        batches.put(batchId, new BatchRun());
        return batchId;
    }

    @Override
    public void uploadChunk(String batchId, List<PriceRecord> records) {
        getBatch(batchId).addRecords(records);
    }

    @Override
    public synchronized void completeBatch(String batchId) {
        BatchRun batch = getBatch(batchId);
        if (batch.getStatus() != BatchStatus.OPEN) {
            throw new IllegalStateException("Batch not open");
        }

        batch.complete();
        batch.getStagedRecords().forEach(store::commit);
        batches.remove(batchId);
    }

    @Override
    public void cancelBatch(String batchId) {
        getBatch(batchId).cancel();
        batches.remove(batchId);
    }

    @Override
    public Optional<PriceRecord> getLastPrice(String instrumentId) {
        return store.getLatest(instrumentId);
    }

    private BatchRun getBatch(String batchId) {
        BatchRun batch = batches.get(batchId);
        if (batch == null) {
            throw new IllegalArgumentException("Unknown batch id");
        }
        return batch;
    }
}

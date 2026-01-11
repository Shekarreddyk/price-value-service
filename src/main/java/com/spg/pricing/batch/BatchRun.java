package com.spg.pricing.batch;

import com.spg.pricing.model.PriceRecord;

import java.util.ArrayList;
import java.util.List;

public class BatchRun {
    private BatchStatus status = BatchStatus.OPEN;
    private final List<PriceRecord> stagedRecords = new ArrayList<>();

    public void addRecords(List<PriceRecord> records) {
        if (status != BatchStatus.OPEN) {
            throw new IllegalStateException("Batch is not open");
        }
        stagedRecords.addAll(records);
    }

    public List<PriceRecord> getStagedRecords() {
        return List.copyOf(stagedRecords);
    }

    public void complete() {
        status = BatchStatus.COMPLETED;
    }

    public void cancel() {
        status = BatchStatus.CANCELLED;
        stagedRecords.clear();
    }
    public BatchStatus getStatus() {
        return status;
    }
}

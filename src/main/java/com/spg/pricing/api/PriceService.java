package com.spg.pricing.api;

import com.spg.pricing.model.PriceRecord;

import java.util.List;
import java.util.Optional;

public interface PriceService {

        String startBatch();

        void uploadChunk(String batchId, List<PriceRecord> records);

        void completeBatch(String batchId);

        void cancelBatch(String batchId);

        Optional<PriceRecord> getLastPrice(String instrumentId);
    }


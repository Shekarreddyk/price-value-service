package com.spg.pricing;

import com.spg.pricing.model.PriceRecord;
import com.spg.pricing.service.InMemoryPriceService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PricingServiceTest {

	@Test
	void pricesVisibleOnlyAfterCompletion() {
		InMemoryPriceService service = new InMemoryPriceService();

		String batch = service.startBatch();
		service.uploadChunk(batch, List.of(
				new PriceRecord("AAPL", Instant.now(), Map.of("price", 100))
		));

		assertTrue(service.getLastPrice("AAPL").isEmpty());

		service.completeBatch(batch);

		assertEquals(100, service.getLastPrice("AAPL").get().getPayload().get("price"));
	}

	@Test
	void cancelledBatchIsDiscarded() {
		InMemoryPriceService service = new InMemoryPriceService();

		String batch = service.startBatch();
		service.uploadChunk(batch, List.of(
				new PriceRecord("IBM", Instant.now(), Map.of("price", 50))
		));

		service.cancelBatch(batch);

		assertTrue(service.getLastPrice("IBM").isEmpty());
	}
	@Test
	void latestAsOfWins() {
		InMemoryPriceService service = new InMemoryPriceService();

		String b1 = service.startBatch();
		service.uploadChunk(b1, List.of(
				new PriceRecord("GOOG", Instant.parse("2024-01-01T10:00:00Z"), Map.of("price", 100))
		));
		service.completeBatch(b1);

		String b2 = service.startBatch();
		service.uploadChunk(b2, List.of(
				new PriceRecord("GOOG", Instant.parse("2024-01-02T10:00:00Z"), Map.of("price", 200))
		));
		service.completeBatch(b2);
		assertEquals(200, service.getLastPrice("GOOG").get().getPayload().get("price"));
	}

}

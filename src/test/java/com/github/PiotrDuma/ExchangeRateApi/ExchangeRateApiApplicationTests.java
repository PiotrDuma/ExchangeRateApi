package com.github.PiotrDuma.ExchangeRateApi;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IT")
@ActiveProfiles(profiles = "test")
class ExchangeRateApiApplicationTests {

	@Test
	void contextLoads() {
	}

}

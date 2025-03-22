package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for a mock bean where the mocked interface has an async method.
 */
@ExtendWith(SpringExtension.class)
class MockitoBeanWithAsyncInterfaceMethodIntegrationTests {

	@MockitoBean
	private Transformer transformer;

	@Autowired
	private MyService service;

	@Test
	void mockedMethodsAreNotAsync() {
		given(this.transformer.transform("foo")).willReturn("bar");
		assertThat(this.service.transform("foo")).isEqualTo("bar");
	}

	interface Transformer {

		@Async
		String transform(String input);

	}

	static class MyService {

		private final Transformer transformer;

		MyService(Transformer transformer) {
			this.transformer = transformer;
		}

		String transform(String input) {
			return this.transformer.transform(input);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@EnableAsync
	static class MyConfiguration {

		@Bean
		MyService myService(Transformer transformer) {
			return new MyService(transformer);
		}

	}

}

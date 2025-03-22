package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for a mock bean where the class being mocked uses field injection.
 *
 */
@ExtendWith(SpringExtension.class)
class MockitoBeanWithInjectedFieldIntegrationTests {

	@MockitoBean
	private MyService myService;

	@Test
	void fieldInjectionIntoMyServiceMockIsNotAttempted() {
		given(this.myService.getCount()).willReturn(5);
		assertThat(this.myService.getCount()).isEqualTo(5);
	}

	static class MyService {

		@Autowired
		private MyRepository repository;

		int getCount() {
			return this.repository.findAll().size();
		}

	}

	interface MyRepository {

		List<Object> findAll();

	}

}

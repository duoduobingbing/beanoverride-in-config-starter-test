

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleStringGenericService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockingDetails;
import org.mockito.Mockito;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link MockitoSpyBean @MockitoSpyBean} on a test class field can be used to inject a spy
 * instance when there are multiple candidates and one is chosen using the name attribute.
 *
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanWithNameOnTestFieldForMultipleExistingBeansTests {

	@MockitoSpyBean(name = "two")
	private SimpleExampleStringGenericService spy;

	@Test
	void testSpying() {
		MockingDetails mockingDetails = Mockito.mockingDetails(this.spy);
		assertThat(mockingDetails.isSpy()).isTrue();
		assertThat(mockingDetails.getMockCreationSettings().getMockName()).hasToString("two");
	}

	@Configuration(proxyBeanMethods = false)
	static class Config {

		@Bean
		SimpleExampleStringGenericService one() {
			return new SimpleExampleStringGenericService("one");
		}

		@Bean
		SimpleExampleStringGenericService two() {
			return new SimpleExampleStringGenericService("two");
		}

	}

}

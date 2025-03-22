package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;


import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleGenericStringServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleStringGenericService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} on a test class field can be used to inject a spy
 * instance when there are multiple candidates and one is primary.
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanOnTestFieldForMultipleExistingBeansWithOnePrimaryIntegrationTests {

	@MockitoSpyBean
	private SimpleExampleStringGenericService spy;

	@Autowired
	private ExampleGenericStringServiceCaller caller;

	@Test
	void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say two");
		assertThat(Mockito.mockingDetails(this.spy).getMockCreationSettings().getMockName()).hasToString("two");
		then(this.spy).should().greeting();
	}

	@Configuration(proxyBeanMethods = false)
	@Import(ExampleGenericStringServiceCaller.class)
	static class Config {

		@Bean
		SimpleExampleStringGenericService one() {
			return new SimpleExampleStringGenericService("one");
		}

		@Bean
		@Primary
		SimpleExampleStringGenericService two() {
			return new SimpleExampleStringGenericService("two");
		}

	}

}

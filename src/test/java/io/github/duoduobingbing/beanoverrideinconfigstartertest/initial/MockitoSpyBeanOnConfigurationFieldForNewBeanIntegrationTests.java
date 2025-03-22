
package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

/**
 * Test {@link MockitoSpyBean @MockitoSpyBean} on a field on a {@code @Configuration} class can be used
 * to inject new spy instances.
 *
 */
@ExtendWith(SpringExtension.class)
@Import(SimpleExampleService.class)
class MockitoSpyBeanOnConfigurationFieldForNewBeanIntegrationTests {

	@Autowired
	private Config config;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say simple");
		then(this.config.exampleService).should().greeting();
	}

	@Configuration(proxyBeanMethods = false)
	@Import(ExampleServiceCaller.class)
	static class Config {

		@MockitoSpyBean
		private SimpleExampleService exampleService;

	}

}

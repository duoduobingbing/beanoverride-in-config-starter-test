

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
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
 * to replace existing beans.
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanOnConfigurationFieldForExistingBeanIntegrationTests {

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
	@Import({ ExampleServiceCaller.class, SimpleExampleService.class })
	static class Config {

		@MockitoSpyBean
		private IExampleService exampleService;

	}

}

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test {@link MockitoBean @MockitoBean} on a configuration class can be used to inject new mock
 * instances.
 *
 */
@ExtendWith(SpringExtension.class)
class MockitoBeanOnConfigurationClassForNewBeanIntegrationTests {

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testMocking() {
		given(this.caller.getService().greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say Boot");
	}

	@Configuration(proxyBeanMethods = false)
	@MockitoBean(types = IExampleService.class)
	@Import(ExampleServiceCaller.class)
	static class Config {

	}

}

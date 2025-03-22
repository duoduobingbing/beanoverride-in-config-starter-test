package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.FailingExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
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
 * Test {@link MockitoBean @MockitoBean} on a configuration class can be used to replace
 * existing beans.
 *
 */
@ExtendWith(SpringExtension.class)
class MockitoBeanOnConfigurationClassForExistingBeanIntegrationTests {

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testMocking() {
		given(this.caller.getService().greeting()).willReturn("XYZ");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say XYZ");
	}

	@Configuration(proxyBeanMethods = false)
	@MockitoBean(types = IExampleService.class)
	@Import({ ExampleServiceCaller.class, FailingExampleService.class })
	static class Config {

	}

}

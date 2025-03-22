
package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Integration tests for using {@link org.springframework.test.context.bean.override.mockito.MockitoBean @MockitoBean} with
 * {@link DirtiesContext @DirtiesContext} and {@link ClassMode#BEFORE_EACH_TEST_METHOD}.
 *
 */
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class MockitoBeanWithDirtiesContextClassModeBeforeMethodIntegrationTests {

	@MockitoBean
	private IExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testMocking() {
		given(this.exampleService.greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say Boot");
	}

	@Configuration(proxyBeanMethods = false)
	@Import(ExampleServiceCaller.class)
	static class Config {

	}

}

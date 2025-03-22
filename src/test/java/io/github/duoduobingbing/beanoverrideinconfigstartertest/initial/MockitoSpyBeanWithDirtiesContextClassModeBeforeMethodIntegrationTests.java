package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.BDDMockito.then;

/**
 * Integration tests for using {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} with
 * {@link DirtiesContext @DirtiesContext} and {@link ClassMode#BEFORE_EACH_TEST_METHOD}.
 *
 */
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(SimpleExampleService.class)
class MockitoSpyBeanWithDirtiesContextClassModeBeforeMethodIntegrationTests {

	@MockitoSpyBean
	private SimpleExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testSpying() {
		this.caller.sayGreeting();
		then(this.exampleService).should().greeting();
	}

	@Configuration(proxyBeanMethods = false)
	@Import(ExampleServiceCaller.class)
	static class Config {

	}

}

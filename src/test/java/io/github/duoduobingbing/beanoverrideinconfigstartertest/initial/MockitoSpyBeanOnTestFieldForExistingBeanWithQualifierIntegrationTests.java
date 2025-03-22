

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.MyCustomQualifier;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.CustomQualifierExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.RealExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} on a test class field can be used to replace existing
 * bean while preserving qualifiers.
 *
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanOnTestFieldForExistingBeanWithQualifierIntegrationTests {

	@MockitoSpyBean
	@MyCustomQualifier
	private IExampleService service;

	@Autowired
	private ExampleServiceCaller caller;

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void testMocking() {
		this.caller.sayGreeting();
		then(this.service).should().greeting();
	}

	@Test
	void onlyQualifiedBeanIsReplaced() {
		assertThat(this.applicationContext.getBean("service")).isSameAs(this.service);
		IExampleService anotherService = this.applicationContext.getBean("anotherService", IExampleService.class);
		assertThat(anotherService.greeting()).isEqualTo("Another");
	}

	@Configuration(proxyBeanMethods = false)
	static class TestConfig {

		@Bean
		CustomQualifierExampleService service() {
			return new CustomQualifierExampleService();
		}

		@Bean
		IExampleService anotherService() {
			return new RealExampleService("Another");
		}

		@Bean
		ExampleServiceCaller controller(@MyCustomQualifier IExampleService service) {
			return new ExampleServiceCaller(service);
		}

	}

}

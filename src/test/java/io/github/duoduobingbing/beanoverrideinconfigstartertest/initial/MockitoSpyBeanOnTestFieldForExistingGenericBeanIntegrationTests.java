

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleGenericService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleGenericServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleIntegerGenericService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleStringGenericService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} on a test class field can be used to replace existing
 * beans.
 *
 * @see MockitoSpyBeanOnTestFieldForExistingBeanCacheIntegrationTests
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanOnTestFieldForExistingGenericBeanIntegrationTests {

	// gh-7625

	@MockitoSpyBean
	private ExampleGenericService<String> exampleService;

	@Autowired
	private ExampleGenericServiceCaller caller;

	@Test
	void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say 123 simple");
		then(this.exampleService).should().greeting();
	}

	@Configuration(proxyBeanMethods = false)
	@Import({ ExampleGenericServiceCaller.class, SimpleExampleIntegerGenericService.class })
	static class SpyBeanOnTestFieldForExistingBeanConfig {

		@Bean
		ExampleGenericService<String> simpleExampleStringGenericService() {
			// In order to trigger issue we need a method signature that returns the
			// generic type not the actual implementation class
			return new SimpleExampleStringGenericService();
		}

	}

}

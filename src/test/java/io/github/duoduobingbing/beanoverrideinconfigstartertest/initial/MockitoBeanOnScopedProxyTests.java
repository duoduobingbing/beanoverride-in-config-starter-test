package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.FailingExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test {@link MockitoBean @MockitoBean} when used in combination with scoped proxy targets.
 *
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/5724">gh-5724</a>
 */
@ExtendWith(SpringExtension.class)
class MockitoBeanOnScopedProxyTests {

	@MockitoBean
	private IExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testMocking() {
		given(this.caller.getService().greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say Boot");
	}

	@Configuration(proxyBeanMethods = false)
	@Import({ ExampleServiceCaller.class })
	static class Config {

		@Bean
		@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
		IExampleService exampleService() {
			return new FailingExampleService();
		}

	}

}

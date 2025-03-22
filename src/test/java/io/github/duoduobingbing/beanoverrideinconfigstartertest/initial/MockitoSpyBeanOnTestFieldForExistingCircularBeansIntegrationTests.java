

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.then;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} on a test class field can be used to replace existing
 * beans with circular dependencies.
 */
@ExtendWith({SpringExtension.class})
@ContextConfiguration(
		classes = MockitoSpyBeanOnTestFieldForExistingCircularBeansIntegrationTests.MockitoSpyBeanOnTestFieldForExistingCircularBeansConfig.class
)
class MockitoSpyBeanOnTestFieldForExistingCircularBeansIntegrationTests {

	@MockitoSpyBean
	private One one;

	@Autowired
	private Two two;

	@Test
	void beanWithCircularDependenciesCanBeSpied() {
		this.two.callOne();
		then(this.one).should().someMethod();
	}

	@Import({ One.class, Two.class })
	static class MockitoSpyBeanOnTestFieldForExistingCircularBeansConfig {

	}

	static class One {

		@Autowired
		@SuppressWarnings("unused")
		private Two two;

		void someMethod() {

		}

	}

	static class Two {

		@Autowired
		private One one;

		void callOne() {
			this.one.someMethod();
		}

	}

}

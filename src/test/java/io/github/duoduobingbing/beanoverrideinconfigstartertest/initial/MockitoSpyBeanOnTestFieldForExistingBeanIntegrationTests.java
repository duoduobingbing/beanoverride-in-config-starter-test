
package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(classes = MockitoSpyBeanOnTestFieldForExistingBeanConfig.class)
class MockitoSpyBeanOnTestFieldForExistingBeanIntegrationTests {

	@MockitoSpyBean
	private IExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testSpying() {
		assertThat(this.caller.sayGreeting()).isEqualTo("I say simple");
		then(this.caller.getService()).should().greeting();
	}

}

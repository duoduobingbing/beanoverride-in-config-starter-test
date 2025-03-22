package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoBean @MockitoBean} on a test class field can be used to replace existing
 * beans.
 *
 * @see MockitoBeanOnTestFieldForExistingBeanCacheIntegrationTests
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MockitoBeanOnTestFieldForExistingBeanConfig.class)
class MockitoBeanOnTestFieldForExistingBeanIntegrationTests {

	@MockitoBean
	private IExampleService exampleService;

	@Autowired
	private ExampleServiceCaller caller;

	@Test
	void testMocking() {
		given(this.exampleService.greeting()).willReturn("Boot");
		assertThat(this.caller.sayGreeting()).isEqualTo("I say Boot");
	}

}

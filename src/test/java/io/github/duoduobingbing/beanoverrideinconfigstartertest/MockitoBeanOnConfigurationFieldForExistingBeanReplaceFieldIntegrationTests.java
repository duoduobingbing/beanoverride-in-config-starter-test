package io.github.duoduobingbing.beanoverrideinconfigstartertest;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.FailingExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test {@link MockitoBean @MockitoBean} on a field on a {@code @Configuration} class can be
 * used to replace existing beans.
 *
 */
@ExtendWith({SpringExtension.class})
class MockitoBeanOnConfigurationFieldForExistingBeanReplaceFieldIntegrationTests {

    @Autowired
    private Config config;

    @Autowired
    private ExampleServiceCaller caller;

    @Test
    void testMocking() {
        Assertions.assertThat(this.config.exampleService).isNotNull();
        BDDMockito.given(this.config.exampleService.greeting()).willReturn("Boot");
        Assertions.assertThat(caller.sayGreeting()).isEqualTo("I say Boot");
    }

    @Configuration(proxyBeanMethods = false)
    @Import({ ExampleServiceCaller.class, FailingExampleService.class })
    static class Config {

        @MockitoBean
        private IExampleService exampleService;

    }

}

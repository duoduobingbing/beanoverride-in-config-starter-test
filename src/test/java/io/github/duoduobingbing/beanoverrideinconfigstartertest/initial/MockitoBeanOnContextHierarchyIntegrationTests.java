package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoBean @MockitoBean} can be used with a
 * {@link ContextHierarchy @ContextHierarchy}.
 */
@ExtendWith(SpringExtension.class)
@ContextHierarchy(
        {
                @ContextConfiguration(classes = MockitoBeanOnContextHierarchyIntegrationTests.ParentConfig.class),
                @ContextConfiguration(classes = MockitoBeanOnContextHierarchyIntegrationTests.ChildConfig.class)
        }
)
class MockitoBeanOnContextHierarchyIntegrationTests {

    @Autowired
    private ChildConfig childConfig;

    @Test
    void testMocking() {
        ApplicationContext context = this.childConfig.getContext();
        ApplicationContext parentContext = context.getParent();

        assertThat(parentContext
                .getBeanNamesForType(IExampleService.class)).hasSize(1);
        assertThat(parentContext
                .getBeanNamesForType(ExampleServiceCaller.class))
                .isEmpty();
        assertThat(context.getBeanNamesForType(IExampleService.class))
                .isEmpty();
        assertThat(context
                .getBeanNamesForType(ExampleServiceCaller.class))
                .hasSize(1);
        assertThat(context.getBean(IExampleService.class))
                .isNotNull();
        assertThat(context.getBean(ExampleServiceCaller.class))
                .isNotNull();
    }

    @Configuration(proxyBeanMethods = false)
    @MockitoBean(types = IExampleService.class)
    static class ParentConfig {

    }

    @Configuration(proxyBeanMethods = false)
    @MockitoBean(types = ExampleServiceCaller.class)
    static class ChildConfig implements ApplicationContextAware {

        private ApplicationContext context;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.context = applicationContext;
        }

        ApplicationContext getContext() {
            return this.context;
        }

    }

}

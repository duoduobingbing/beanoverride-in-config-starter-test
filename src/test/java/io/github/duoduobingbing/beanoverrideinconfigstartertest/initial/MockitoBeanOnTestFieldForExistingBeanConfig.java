package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.FailingExampleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Config for {@link MockBeanOnTestFieldForExistingBeanIntegrationTests} and
 * {@link MockBeanOnTestFieldForExistingBeanCacheIntegrationTests}. Extracted to a shared
 * config to trigger caching.
 */
@Configuration(proxyBeanMethods = false)
@Import({ ExampleServiceCaller.class, FailingExampleService.class })
public class MockitoBeanOnTestFieldForExistingBeanConfig {

}

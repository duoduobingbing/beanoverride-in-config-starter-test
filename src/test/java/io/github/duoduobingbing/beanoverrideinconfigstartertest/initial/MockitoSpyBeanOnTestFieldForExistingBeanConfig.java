

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleServiceCaller;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Config for {@link MockitoSpyBeanOnTestFieldForExistingBeanIntegrationTests} and
 * {@link MockitoSpyBeanOnTestFieldForExistingBeanCacheIntegrationTests}. Extracted to a shared
 * config to trigger caching.
 *
 */
@Configuration(proxyBeanMethods = false)
@Import({ ExampleServiceCaller.class, SimpleExampleService.class })
public class MockitoSpyBeanOnTestFieldForExistingBeanConfig {

}

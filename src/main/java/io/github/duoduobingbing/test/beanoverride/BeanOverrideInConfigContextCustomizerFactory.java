package io.github.duoduobingbing.test.beanoverride;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class BeanOverrideInConfigContextCustomizerFactory implements ContextCustomizerFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanOverrideInConfigContextCustomizerFactory.class);

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        logger.debug("[BOINC] " + "Creating BeanOverrideInConfigContextCustomizer...");
        return new BeanOverrideInConfigContextCustomizer(testClass, configAttributes);
    }
}

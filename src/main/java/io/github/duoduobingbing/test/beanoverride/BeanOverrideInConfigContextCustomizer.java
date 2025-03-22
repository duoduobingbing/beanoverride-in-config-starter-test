package io.github.duoduobingbing.test.beanoverride;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.BeanOverrideContextCustomizerWrapper;
import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.BeanOverrideHandlerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.bean.override.BeanOverrideHandler;

import java.util.List;
import java.util.Set;

public class BeanOverrideInConfigContextCustomizer implements ContextCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(BeanOverrideInConfigContextCustomizer.class);


    static List<BeanOverrideHandler> findAllHandlers(Class<?> testClass) {
        return BeanOverrideHandlerWrapper.findAllHandlers(testClass);
    }


    public static final String INFRASTRUCTURE_BEAN_NAME = "io.github.duoduobingbing.test.beanoverride.internalBeanOverrideInConfigPostProcessor";

    Class<?> testClass;
    List<ContextConfigurationAttributes> configAttributes;

    public BeanOverrideInConfigContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        this.testClass = testClass;
        this.configAttributes = configAttributes;
    }


    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        logger.debug("[BOINC] " + "Customize...");

        ConfigurableBeanFactory beanFactory = context.getBeanFactory();
        if (context instanceof BeanDefinitionRegistry registry) {
            beanFactory.registerSingleton(INFRASTRUCTURE_BEAN_NAME, new BeanOverrideInConfigPostProcessor(registry, beanFactory, testClass));
        }

        //By default the original org.springframework.test.context.bean.override.BeanOverrideContextCustomizer is not created when the
        //org.springframework.test.context.bean.override.BeanOverrideContextCustomizerFactory detects that there a no BeanHandlers found by the default implementation
        //but since we depend on these beans being present later on, we initialize these anyway when no BeanHandlers where found by the default implementation
        //see org.springframework.test.context.bean.override.BeanOverrideContextCustomizerFactory#createContextCustomizer(..) for more details
        if (findAllHandlers(testClass).isEmpty()) {
            invokeBeanOverrideContextCustomizerWhenNoDefaultHandlers(context, mergedConfig);
        }
    }

    public void invokeBeanOverrideContextCustomizerWhenNoDefaultHandlers(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        BeanOverrideContextCustomizerWrapper
                .create(Set.of())
                .get()
                .customizeContext(context, mergedConfig);
    }
}

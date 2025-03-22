package io.github.duoduobingbing.test.beanoverride;

import io.github.duoduobingbing.test.beanoverride.BeanOverrideDefinitionsParser.BeanOverrideHandlersPOJO;
import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.BeanOverrideBeanFactoryPostProcessorWrapper;
import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.BeanOverrideContextCustomizerWrapper;
import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.BeanOverrideRegistryWrapper;
import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.BeanOverrideTestExecutionListenerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.Conventions;
import org.springframework.core.Ordered;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanOverrideInConfigPostProcessor implements InstantiationAwareBeanPostProcessor, BeanClassLoaderAware, BeanFactoryAware, BeanFactoryPostProcessor, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(BeanOverrideInConfigPostProcessor.class);


    private static final String CONFIGURATION_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "configurationClass");

    Map<Field, BeanOverrideHandler> customBeanOverrideHandlerFields = new HashMap<>();


    BeanDefinitionRegistry registry;
    ClassLoader classLoader;
    ConfigurableBeanFactory beanFactory;
    Class<?> testClass;

    public BeanOverrideInConfigPostProcessor(BeanDefinitionRegistry registry, ConfigurableBeanFactory beanFactory, Class<?> testClass) {
        this.registry = registry;
        this.beanFactory = beanFactory;
        this.testClass = testClass;
    }

    static void callDefaultBeanOverrideFactoryPostProcessor(Object beanOverrideRegistry, List<BeanOverrideHandler> handlers, ConfigurableListableBeanFactory beanFactory) {


        BeanFactoryPostProcessor beanOverrideBeanFactoryPostProcessor = (BeanFactoryPostProcessor) beanFactory.getSingleton(BeanOverrideContextCustomizerWrapper.INFRASTRUCTURE_BEAN_NAME);
        BeanOverrideBeanFactoryPostProcessorWrapper beanOverrideBeanFactoryPostProcessorWrapper = BeanOverrideBeanFactoryPostProcessorWrapper.of(beanOverrideBeanFactoryPostProcessor);
        Set<BeanOverrideHandler> oldHandlers = beanOverrideBeanFactoryPostProcessorWrapper.getHandlers();
        beanOverrideBeanFactoryPostProcessorWrapper.setHandlers(new LinkedHashSet<>(handlers)); //set handlers to only the newly discovered ones

        //Caution: Invoking this manually causes the beanName duplication check to not detect collision with beans that would have been found normally by Spring's own implementation
        //TODO: write custom check involving the internal methods in BeanOverrideBeanFactoryPostProcessor
        beanOverrideBeanFactoryPostProcessorWrapper.get().postProcessBeanFactory(beanFactory); //call postProcessBeanFactory(beanFactory) manually again
        LinkedHashSet<BeanOverrideHandler> newHandlersCombined = new LinkedHashSet<>(oldHandlers);
        newHandlersCombined.addAll(handlers);
        beanOverrideBeanFactoryPostProcessorWrapper.setHandlers(newHandlersCombined);

        // <editor-fold defaultstate="collapsed" desc="OLD: directly construct a new BeanOverrideBeanFactoryPostProcessor">
        // BeanOverrideRegistryWrapper beanOverrideRegistryWrapper = BeanOverrideRegistryWrapper.of(beanOverrideRegistry);
//        BeanOverrideBeanFactoryPostProcessorWrapper beanOverrideBeanFactoryPostProcessorWrapper = BeanOverrideBeanFactoryPostProcessorWrapper.create(new HashSet<>(handlers), beanOverrideRegistryWrapper);
//        BeanFactoryPostProcessor beanOverrideBeanFactoryPostProcessor = beanOverrideBeanFactoryPostProcessorWrapper.get();
//        beanOverrideBeanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        // </editor-fold>

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {


        logger.debug("[BOINC] " + "BeanFactoryPostProcessor postProcessBeanFactory");

        Object overridePostProcessor = beanFactory.getSingleton(BeanOverrideContextCustomizerWrapper.INFRASTRUCTURE_BEAN_NAME);
        logger.debug("[BOINC] Found OverridePostProcessor: {}", overridePostProcessor);

        Object registryBean = beanFactory.getSingleton(BeanOverrideRegistryWrapper.BEAN_NAME);
        logger.debug("[BOINC] Found Registry: {}", registryBean);

        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        logger.debug("[BOINC] Found bean names: {}", Arrays.toString(beanDefinitionNames));

        BeanOverrideDefinitionsParser parser = new BeanOverrideDefinitionsParser();
        List<BeanOverrideHandler> allCreatedHandlers = new ArrayList<>();
        for (Class<?> configurationClass : getConfigurationClasses(beanFactory, this.classLoader)) {
            BeanOverrideHandlersPOJO parsedDefinitions = parser.parse(configurationClass, testClass);
            allCreatedHandlers.addAll(parsedDefinitions.classLevelOverrideHandlers());
            allCreatedHandlers.addAll(parsedDefinitions.fieldBeanOverrideHandlerMap().values());
            customBeanOverrideHandlerFields.putAll(parsedDefinitions.fieldBeanOverrideHandlerMap());

        }

        callDefaultBeanOverrideFactoryPostProcessor(registryBean, allCreatedHandlers, beanFactory);
    }

    public Set<Class<?>> getConfigurationClasses(ConfigurableListableBeanFactory beanFactory, ClassLoader classLoader) {
        Set<Class<?>> configurationClasses = new LinkedHashSet<>();
        for (BeanDefinition beanDefinition : getConfigurationBeanDefinitions(beanFactory).values()) {
            configurationClasses.add(ClassUtils.resolveClassName(beanDefinition.getBeanClassName(), classLoader));
        }
        return configurationClasses;
    }

    private static Map<String, BeanDefinition> getConfigurationBeanDefinitions(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanDefinition> definitions = new LinkedHashMap<>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);

            if (definition.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE) == null) {
                continue;
            }

            definitions.put(beanName, definition);
        }
        return definitions;
    }

    //This injects the configuration beans
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), (field) -> postProcessField(bean, field));
        return pvs;
    }

    private void postProcessField(Object bean, Field field) throws BeansException {

        if (customBeanOverrideHandlerFields.isEmpty()) {
            return;
        }

        Object registryBean = beanFactory.getSingleton(BeanOverrideRegistryWrapper.BEAN_NAME);
        BeanOverrideRegistryWrapper wrapper = BeanOverrideRegistryWrapper.of(registryBean);

        BeanOverrideHandler fieldBeanOverrideHandler = customBeanOverrideHandlerFields.get(field);

        if (fieldBeanOverrideHandler == null) { //skip if not found
            return;
        }

        //see BeanOverrideTestExecutionListener#injectFields
        Object gotBean = wrapper.getBeanForHandler(fieldBeanOverrideHandler, field.getType());
        BeanOverrideTestExecutionListenerWrapper.injectField(field, bean, gotBean);

        //TODO: remove
//        wrapper.inject(bean, fieldBeanOverrideHandler);


    }

    @Override
    public int getOrder() {
        //BeanOverrideBeanFactoryPostProcessor.getOrder

        return Ordered.LOWEST_PRECEDENCE - 9;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Assert.notNull(beanFactory, "beanFactory must not be null");

        if (!(beanFactory instanceof ConfigurableBeanFactory beanFactory1)) {
            throw new IllegalArgumentException("beanFactory must be an instance of ConfigurableBeanFactory but was " + beanFactory.getClass().getName());
        }

        this.beanFactory = beanFactory1;
    }
}

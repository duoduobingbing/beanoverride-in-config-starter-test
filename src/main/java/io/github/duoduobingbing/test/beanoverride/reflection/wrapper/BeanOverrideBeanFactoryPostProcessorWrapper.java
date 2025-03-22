package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.context.bean.override.BeanOverrideHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Wrapper for {@link org.springframework.test.context.bean.override.BeanOverrideBeanFactoryPostProcessor}
 */
public class BeanOverrideBeanFactoryPostProcessorWrapper {

    BeanFactoryPostProcessor beanOverrideBeanFactoryPostProcessor;
    static Constructor<? extends BeanFactoryPostProcessor> beanOverrideBeanFactoryPostProcessorConstructor;
    static Class<?> clazz;
    static Field handlersField;
    static Method getBeanNameForTypeMethod;


    static {
        ExecutionHelper.executeMayThrows(() -> {
            clazz = Class.forName("org.springframework.test.context.bean.override.BeanOverrideBeanFactoryPostProcessor");
            @SuppressWarnings("unchecked")
            Constructor<? extends BeanFactoryPostProcessor> constructor = (Constructor<? extends BeanFactoryPostProcessor>) clazz.getDeclaredConstructor(Set.class, Class.forName("org.springframework.test.context.bean.override.BeanOverrideRegistry"));
            beanOverrideBeanFactoryPostProcessorConstructor = constructor;
            beanOverrideBeanFactoryPostProcessorConstructor.setAccessible(true);

            handlersField = clazz.getDeclaredField("beanOverrideHandlers");
            handlersField.setAccessible(true);

            getBeanNameForTypeMethod = clazz.getDeclaredMethod("getBeanNameForType", ConfigurableListableBeanFactory.class, BeanOverrideHandler.class, boolean.class);
            getBeanNameForTypeMethod.setAccessible(true);

        });
    }


    BeanOverrideBeanFactoryPostProcessorWrapper(BeanFactoryPostProcessor beanOverrideBeanFactoryPostProcessor) {
        this.beanOverrideBeanFactoryPostProcessor = beanOverrideBeanFactoryPostProcessor;
    }

    public static BeanOverrideBeanFactoryPostProcessorWrapper create(Set<BeanOverrideHandler> handlers, BeanOverrideRegistryWrapper beanOverrideRegistryWrapper) {
        return ExecutionHelper.executeMayThrows(
                () -> new BeanOverrideBeanFactoryPostProcessorWrapper(beanOverrideBeanFactoryPostProcessorConstructor.newInstance(handlers, beanOverrideRegistryWrapper.get()))
        );
    }

    public static BeanOverrideBeanFactoryPostProcessorWrapper of(BeanFactoryPostProcessor beanOverrideBeanFactoryPostProcessor) {
        if (!clazz.isInstance(beanOverrideBeanFactoryPostProcessor)) {
            throw new IllegalArgumentException("beanOverrideBeanFactoryPostProcessor must be of type %s but was a %s".formatted(clazz.getName(), beanOverrideBeanFactoryPostProcessor.getClass().getName()));
        }

        return new BeanOverrideBeanFactoryPostProcessorWrapper(beanOverrideBeanFactoryPostProcessor);
    }

    public BeanFactoryPostProcessor get() {
        return beanOverrideBeanFactoryPostProcessor;
    }

    @SuppressWarnings("unchecked")
    public Set<BeanOverrideHandler> getHandlers() {
        return ExecutionHelper.executeMayThrows(() -> (Set<BeanOverrideHandler>) handlersField.get(beanOverrideBeanFactoryPostProcessor));
    }

    public void setHandlers(Set<BeanOverrideHandler> handlers) {
        ExecutionHelper.executeMayThrows(() -> handlersField.set(beanOverrideBeanFactoryPostProcessor, handlers));
    }

    public static String getBeanNameForType(ConfigurableListableBeanFactory beanFactory, BeanOverrideHandler handler, boolean requireExistingBean) {
        return ExecutionHelper.executeMayThrows(() -> (String) getBeanNameForTypeMethod.invoke(null, beanFactory, handler, requireExistingBean));
    }


}

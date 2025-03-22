package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.bean.override.BeanOverrideHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Wrapper for {@link org.springframework.test.context.bean.override.BeanOverrideContextCustomizer}
 */
public class BeanOverrideContextCustomizerWrapper {

    ContextCustomizer beanOverrideContextCustomizer;

    static Constructor<? extends ContextCustomizer> beanOverrideContextCustomizerConstructor;

    //TODO: remove
//    static public String REGISTRY_BEAN_NAME = "org.springframework.test.context.bean.override.internalBeanOverrideRegistry";
    static public String INFRASTRUCTURE_BEAN_NAME = "org.springframework.test.context.bean.override.internalBeanOverridePostProcessor";
    static public String EARLY_INFRASTRUCTURE_BEAN_NAME = "org.springframework.test.context.bean.override.internalWrapEarlyBeanPostProcessor";

    public BeanOverrideContextCustomizerWrapper(ContextCustomizer beanOverrideContextCustomizer) {
        this.beanOverrideContextCustomizer = beanOverrideContextCustomizer;
    }

    static {
        ExecutionHelper.executeMayThrows(
                () -> {
                    Class<?> beanOverrideContextCustomizerClass = Class.forName("org.springframework.test.context.bean.override.BeanOverrideContextCustomizer");
                    @SuppressWarnings("unchecked")
                    Constructor<? extends ContextCustomizer> constructor = (Constructor<? extends ContextCustomizer>) beanOverrideContextCustomizerClass.getDeclaredConstructor(Set.class);
                    beanOverrideContextCustomizerConstructor = constructor;
                    beanOverrideContextCustomizerConstructor.setAccessible(true);

//                    Field registryBeanNameField = beanOverrideContextCustomizerClass.getDeclaredField("REGISTRY_BEAN_NAME");
//                    registryBeanNameField.setAccessible(true);
//                    REGISTRY_BEAN_NAME = (String)registryBeanNameField.get(null);

                    Field infrastructureBeanNameField = beanOverrideContextCustomizerClass.getDeclaredField("INFRASTRUCTURE_BEAN_NAME");
                    infrastructureBeanNameField.setAccessible(true);
                    INFRASTRUCTURE_BEAN_NAME = (String)infrastructureBeanNameField.get(null);

                    Field earlyInfrastructureBeanName = beanOverrideContextCustomizerClass.getDeclaredField("EARLY_INFRASTRUCTURE_BEAN_NAME");
                    earlyInfrastructureBeanName.setAccessible(true);
                    EARLY_INFRASTRUCTURE_BEAN_NAME = (String)earlyInfrastructureBeanName.get(null);
                }
        );
    }

    public static BeanOverrideContextCustomizerWrapper create(Set<BeanOverrideHandler> handlers) {
        return ExecutionHelper.executeMayThrows(() -> new BeanOverrideContextCustomizerWrapper(beanOverrideContextCustomizerConstructor.newInstance(handlers)));
    }

    public ContextCustomizer get() {
        return beanOverrideContextCustomizer;
    }


}

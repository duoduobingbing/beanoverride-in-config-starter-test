package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.test.context.bean.override.BeanOverrideHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Wrapper for {@link org.springframework.test.context.bean.override.BeanOverrideRegistry}
 */
public class BeanOverrideRegistryWrapper {

    public static String BEAN_NAME;

    Object beanOverrideRegistry;
    static Class<?> beanOverrideRegistryClass;

    //TODO: remove
//    static Method injectMethod1;
//    static Method injectMethod2;

    static Method getBeanForHandlerMethod;

    static {
        ExecutionHelper.executeMayThrows(() -> {
            beanOverrideRegistryClass = Class.forName("org.springframework.test.context.bean.override.BeanOverrideRegistry");

//            injectMethod1 = beanOverrideRegistryClass.getDeclaredMethod("inject", Object.class, BeanOverrideHandler.class);
//            injectMethod1.setAccessible(true);
//
//            injectMethod2 = beanOverrideRegistryClass.getDeclaredMethod("inject", Field.class, Object.class, String.class);
//            injectMethod2.setAccessible(true);

            getBeanForHandlerMethod = beanOverrideRegistryClass.getDeclaredMethod("getBeanForHandler", BeanOverrideHandler.class, Class.class);
            getBeanForHandlerMethod.setAccessible(true);

            Field beanNameField = beanOverrideRegistryClass.getDeclaredField("BEAN_NAME");
            beanNameField.setAccessible(true);
            BEAN_NAME = (String)beanNameField.get(null);

        });
    }

    BeanOverrideRegistryWrapper(Object beanOverrideRegistry) {
        this.beanOverrideRegistry = beanOverrideRegistry;
    }

    public static BeanOverrideRegistryWrapper of(Object beanOverrideRegistry) {
        if (!beanOverrideRegistryClass.isInstance(beanOverrideRegistry)) {
            throw new IllegalArgumentException("beanOverrideRegistry has to be a %s but was a %s".formatted(beanOverrideRegistryClass, Optional.ofNullable(beanOverrideRegistry).map(Object::getClass).orElse(null)));
        }

        return new BeanOverrideRegistryWrapper(beanOverrideRegistry);
    }

    public Object get() {
        return beanOverrideRegistry;
    }

//    public void inject(Object target, BeanOverrideHandler handler) {
//        ExecutionHelper.executeMayThrows(() -> {
//            injectMethod1.invoke(beanOverrideRegistry, target, handler);
//        });
//    }
//
//    public void inject(Field field, Object target, String beanName) {
//        ExecutionHelper.executeMayThrows(() -> {
//            injectMethod2.invoke(beanOverrideRegistry, field, target, beanName);
//        });
//    }

    public Object getBeanForHandler(BeanOverrideHandler handler, Class<?> requiredType) {
        return ExecutionHelper.executeMayThrows(() -> getBeanForHandlerMethod.invoke(beanOverrideRegistry, handler, requiredType));
    }
}

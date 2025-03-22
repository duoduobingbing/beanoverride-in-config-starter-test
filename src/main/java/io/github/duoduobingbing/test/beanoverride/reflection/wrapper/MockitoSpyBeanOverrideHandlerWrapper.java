package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Wrapper for {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBeanOverrideHandler}
 */
public class MockitoSpyBeanOverrideHandlerWrapper {

    BeanOverrideHandler mockitoSpyBeanOverrideHandler;

    static Constructor<? extends BeanOverrideHandler> mockitoSpyBeanOverrideHandlerConstructor;

    static {
        ExecutionHelper.executeMayThrows(() -> {
            Class<?> mockitoSpyBeanOverrideHandlerClass = Class.forName("org.springframework.test.context.bean.override.mockito.MockitoSpyBeanOverrideHandler");
            @SuppressWarnings("unchecked")
            Constructor<? extends BeanOverrideHandler> constructor = (Constructor<? extends BeanOverrideHandler>) mockitoSpyBeanOverrideHandlerClass.getDeclaredConstructor(Field.class, ResolvableType.class, MockitoSpyBean.class);
            mockitoSpyBeanOverrideHandlerConstructor = constructor;
            mockitoSpyBeanOverrideHandlerConstructor.setAccessible(true);
        });
    }

    MockitoSpyBeanOverrideHandlerWrapper(BeanOverrideHandler mockitoSpyBeanOverrideHandler) {
        this.mockitoSpyBeanOverrideHandler = mockitoSpyBeanOverrideHandler;
    }

    public static MockitoSpyBeanOverrideHandlerWrapper create(Field field, ResolvableType resolvableType, MockitoSpyBean mockitoSpyBeanAnnotation) {
        return ExecutionHelper.executeMayThrows(() -> new MockitoSpyBeanOverrideHandlerWrapper(mockitoSpyBeanOverrideHandlerConstructor.newInstance(field, resolvableType, mockitoSpyBeanAnnotation)));
    }

    public BeanOverrideHandler get() {
        return mockitoSpyBeanOverrideHandler;
    }
}

package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Wrapper for {@link org.springframework.test.context.bean.override.mockito.MockitoBeanOverrideHandler}
 */
public class MockitoBeanOverrideHandlerWrapper {

    BeanOverrideHandler mockitoBeanOverrideHandler;

    static Constructor<? extends BeanOverrideHandler> mockitoBeanOverrideHandlerConstructor;
    static {
        ExecutionHelper.executeMayThrows(() -> {
            Class<?> mockitoBeanOverrideHandlerClass = Class.forName("org.springframework.test.context.bean.override.mockito.MockitoBeanOverrideHandler");
            @SuppressWarnings("unchecked")
            Constructor<? extends BeanOverrideHandler> constructor = (Constructor<? extends BeanOverrideHandler>)mockitoBeanOverrideHandlerClass.getDeclaredConstructor(Field.class, ResolvableType.class, MockitoBean.class);
            mockitoBeanOverrideHandlerConstructor = constructor;
            mockitoBeanOverrideHandlerConstructor.setAccessible(true);
        });
    }

    MockitoBeanOverrideHandlerWrapper(BeanOverrideHandler mockitoBeanOverrideHandler) {
        this.mockitoBeanOverrideHandler = mockitoBeanOverrideHandler;
    }

    public static MockitoBeanOverrideHandlerWrapper create(Field field, ResolvableType resolvableType, MockitoBean mockitoBeanAnnotation) {
        return ExecutionHelper.executeMayThrows(() -> new MockitoBeanOverrideHandlerWrapper(mockitoBeanOverrideHandlerConstructor.newInstance(field, resolvableType, mockitoBeanAnnotation)));
    }

    public BeanOverrideHandler get(){
        return mockitoBeanOverrideHandler;
    }
}

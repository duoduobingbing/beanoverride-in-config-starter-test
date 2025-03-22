package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.test.context.bean.override.BeanOverrideHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Wrapper for non-public methods of {@link org.springframework.test.context.bean.override.BeanOverrideHandler}
 */
public class BeanOverrideHandlerWrapper {

    BeanOverrideHandler beanOverrideHandler;

    static Method allHandlersMethod;

    static {

        ExecutionHelper.executeMayThrows(() -> {
            allHandlersMethod = BeanOverrideHandler.class.getDeclaredMethod("findAllHandlers", Class.class);
            allHandlersMethod.setAccessible(true);
        });
    }

    BeanOverrideHandlerWrapper(BeanOverrideHandler beanOverrideHandler) {
        this.beanOverrideHandler = beanOverrideHandler;
    }



    @SuppressWarnings("unchecked")
    public static List<BeanOverrideHandler> findAllHandlers(Class<?> testClass) {
        return ExecutionHelper.executeMayThrows(() -> {
            return (List<BeanOverrideHandler>)allHandlersMethod.invoke(null, testClass);
        });
    }
}

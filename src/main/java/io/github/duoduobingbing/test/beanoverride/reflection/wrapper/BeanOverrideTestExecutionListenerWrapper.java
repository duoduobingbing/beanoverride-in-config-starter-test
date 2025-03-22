package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.helper.ExecutionHelper;
import org.springframework.test.context.bean.override.BeanOverrideTestExecutionListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanOverrideTestExecutionListenerWrapper {

    static Method injectFieldMethod;

    static {
        ExecutionHelper.executeMayThrows(() -> {
            injectFieldMethod = BeanOverrideTestExecutionListener.class.getDeclaredMethod("injectField", Field.class, Object.class, Object.class);
            injectFieldMethod.setAccessible(true);
        });
    }

    public static void injectField(Field field, Object target, Object bean) {
        ExecutionHelper.executeMayThrows(() -> {
            injectFieldMethod.invoke(null, field, target, bean);
        });
    }
}

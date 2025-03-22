package io.github.duoduobingbing.test.beanoverride.definition;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.MockitoSpyBeanOverrideHandlerWrapper;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 * Specific implementation of {@link BeanOverrideSpecificAnnotation} for {@link MockitoSpyBean} annotations
 */
public final class BeanOverrideMockitoSpyAnnotation extends BeanOverrideSpecificAnnotation<MockitoSpyBean> {



    BeanOverrideMockitoSpyAnnotation(MockitoSpyBean annotation) {
        super(annotation);
    }

    /**
     * Provides access to {@link MockitoSpyBean#types()}
     * @see MockitoSpyBean#types()
     * @return types (declared by a class-level annotation)
     */
    @Override
    public Class<?>[] getTypes() {
        return annotation.types();
    }

    /**
     * Provides access to {@link MockitoSpyBean#name()}
     * @see MockitoSpyBean#name()
     * @return bean name
     */
    @Override
    public String getName() {
        return annotation.name();
    }

    /**
     * Creates a MockitoSpyBeanOverrideHandler for a specific field.
     * This directly creates a BeanOverrideHandler and skips its default creation via BeanOverrideProcessor.createHandler(...)
     * @param resolvableType type of the field
     * @param testClass class with tests
     * @param field the field with the mock inside
     * @return The constructed MockitoSpyBeanOverrideHandler
     */
    @Override
    public BeanOverrideHandler createBeanOverrideHandler(ResolvableType resolvableType, Class<?> testClass, Field field) {
        Assert.state(annotation.types().length == 0,"The @MockitoSpyBean 'types' attribute must be omitted when declared on a field");
        return MockitoSpyBeanOverrideHandlerWrapper.create(field, resolvableType, annotation).get();
    }

}

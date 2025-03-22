package io.github.duoduobingbing.test.beanoverride.definition;

import io.github.duoduobingbing.test.beanoverride.reflection.wrapper.MockitoBeanOverrideHandlerWrapper;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.Assert;

import java.lang.reflect.Field;

/**
 * Specific implementation of {@link BeanOverrideSpecificAnnotation} for {@link MockitoBean} annotations
 */
public final class BeanOverrideMockitoMockBeanAnnotation extends BeanOverrideSpecificAnnotation<MockitoBean> {

    BeanOverrideMockitoMockBeanAnnotation(MockitoBean annotation) {
        super(annotation);
    }

    /**
     * Provides access to {@link MockitoBean#types()}
     * @see MockitoBean#types()
     * @return types (declared by a class-level annotation)
     */
    @Override
    public Class<?>[] getTypes() {
        return annotation.types();
    }

    /**
     * Provides access to {@link MockitoBean#name()}
     * @see MockitoBean#name()
     * @return bean name
     */
    @Override
    public String getName() {
        return annotation.name();
    }

    /**
     * Creates a MockitoBeanOverrideHandler for a specific field.
     * This directly creates a BeanOverrideHandler and skips its default creation via BeanOverrideProcessor.createHandler(...)
     * @param resolvableType type of the field
     * @param testClass class with tests
     * @param field the field with the mock inside
     * @return The constructed MockitoBeanOverrideHandler
     */
    @Override
    public BeanOverrideHandler createBeanOverrideHandler(ResolvableType resolvableType, Class<?> testClass, Field field) {
        Assert.state(annotation.types().length == 0,"The @MockitoBean 'types' attribute must be omitted when declared on a field");
        return MockitoBeanOverrideHandlerWrapper.create(field, resolvableType, annotation).get();
    }

}

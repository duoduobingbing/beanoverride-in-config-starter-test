package io.github.duoduobingbing.test.beanoverride.definition;

import org.springframework.core.ResolvableType;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Wrapper class to get a common access point to traits shared by {@link MockitoBean} and  {@link MockitoSpyBean}
 * @param <E>
 */
public abstract sealed class BeanOverrideSpecificAnnotation<E extends Annotation> permits BeanOverrideMockitoMockBeanAnnotation, BeanOverrideMockitoSpyAnnotation{

    E annotation;

    BeanOverrideSpecificAnnotation(E annotation) {
        this.annotation = annotation;
    }


    /**
     * Constructs a BeanOverrideSpecificAnnotation based on the annotation. If annotation is an annotation unsupported by BeanOverrideSpecificAnnotation (i.e. neither {@link MockitoBean} nor {@link MockitoSpyBean}) it will return an empty Optional.
     * @param annotation Should be either {@link MockitoBean} or {@link MockitoSpyBean}
     * @return An optional containing the common accessor
     * @param <E> Should be either {@link MockitoBean} or {@link MockitoSpyBean}
     */
    @SuppressWarnings("unchecked")
    public static <E extends Annotation> Optional<BeanOverrideSpecificAnnotation<E>> of(E annotation) {
        return (Optional<BeanOverrideSpecificAnnotation<E>>) switch(annotation){
            case MockitoSpyBean mockitoSpyBean -> Optional.of(new BeanOverrideMockitoSpyAnnotation(mockitoSpyBean));
            case MockitoBean mockitoBean -> Optional.of(new BeanOverrideMockitoMockBeanAnnotation(mockitoBean));
            default -> Optional.empty();
        };
    }

    /**
     * Provides access to the types provided by a BeanOverrideAnnotation when declared on a class level
     * @return types (empty array when none declared or not on type level)
     */
    public abstract Class<?>[] getTypes();

    /**
     * Provides access to the bean name
     * @return name
     */
    public abstract String getName();

    /**
     * Directly creates a BeanOverrideHandler
     * @see org.springframework.test.context.bean.override.mockito.MockitoBeanOverrideProcessor#createHandler(Annotation, Class, Field)
     * @param resolvableType type of the field
     * @param testClass class with the tests
     * @param field field with mock/spy inside
     * @return Concrete BeanOverrideHandler for the specific annotation
     */
    public abstract BeanOverrideHandler createBeanOverrideHandler(ResolvableType resolvableType, Class<?> testClass, Field field);
}

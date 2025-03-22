package io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper;

import org.mockito.Answers;
import org.mockito.MockSettings;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Remove this class once MockBean is gone in Spring Boot
 */

@SuppressWarnings("removal")
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.boot.test.mock.mockito.MockBean
public @interface MockBeanOld {

    /**
     * The name of the bean to register or replace. If not specified the name will either
     * be generated or, if the mock replaces an existing bean, the existing name will be
     * used.
     * @return the name of the bean
     */
    @AliasFor(attribute = "name", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    String name() default "";

    /**
     * The classes to mock. This is an alias of {@link #classes()} which can be used for
     * brevity if no other attributes are defined. See {@link #classes()} for details.
     * @return the classes to mock
     */
    @AliasFor(attribute = "classes", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    Class<?>[] value() default {};

    /**
     * The classes to mock. Each class specified here will result in a mock being created
     * and registered with the application context. Classes can be omitted when the
     * annotation is used on a field.
     * <p>
     * When {@code @MockBean} also defines a {@code name} this attribute can only contain
     * a single value.
     * <p>
     * If this is the only specified attribute consider using the {@code value} alias
     * instead.
     * @return the classes to mock
     */
    @AliasFor(attribute = "value", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    Class<?>[] classes() default {};

    /**
     * Any extra interfaces that should also be declared on the mock. See
     * {@link MockSettings#extraInterfaces(Class...)} for details.
     * @return any extra interfaces
     */
    @AliasFor(attribute = "extraInterfaces", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    Class<?>[] extraInterfaces() default {};

    /**
     * The {@link Answers} type to use on the mock.
     * @return the answer type
     */
    @AliasFor(attribute = "answer", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    Answers answer() default Answers.RETURNS_DEFAULTS;

    /**
     * If the generated mock is serializable. See {@link MockSettings#serializable()} for
     * details.
     * @return if the mock is serializable
     */
    @AliasFor(attribute = "serializable", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    boolean serializable() default false;

    /**
     * The reset mode to apply to the mock bean. The default is {@link MockReset#AFTER}
     * meaning that mocks are automatically reset after each test method is invoked.
     * @return the reset mode
     */
    @AliasFor(attribute = "reset", annotation = org.springframework.boot.test.mock.mockito.MockBean.class)
    MockReset reset() default MockReset.AFTER;

}

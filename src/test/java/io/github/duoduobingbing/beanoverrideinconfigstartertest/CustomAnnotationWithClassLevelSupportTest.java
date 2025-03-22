package io.github.duoduobingbing.beanoverrideinconfigstartertest;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.example.AThing;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions.JUnitAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.test.context.bean.override.BeanOverride;
import org.springframework.test.context.bean.override.BeanOverrideHandler;
import org.springframework.test.context.bean.override.BeanOverrideProcessor;
import org.springframework.test.context.bean.override.BeanOverrideStrategy;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@SpringJUnitConfig
public class CustomAnnotationWithClassLevelSupportTest {

    @Configuration
    static class MyConfiguration {
        @Bean
        AThing aThing() {
            return new AThing();
        }
    }

    public static class SomeOtherThing extends AThing {
        @Override
        public String get() {
            return "SomethingOther";
        }
    }

    public static class MyBeanOverrideHandler extends BeanOverrideHandler {


        public MyBeanOverrideHandler(Field field, ResolvableType beanType, String beanName, String contextName, BeanOverrideStrategy strategy) {
            super(field, beanType, beanName, contextName, strategy);
        }

        @Override
        protected Object createOverrideInstance(String beanName, BeanDefinition existingBeanDefinition, Object existingBeanInstance) {
            if(existingBeanInstance instanceof AThing && !(existingBeanInstance instanceof SomeOtherThing)) {
                System.out.println("SomeOtherThing createOverrideInstance");
                return new SomeOtherThing();
            }

            if(existingBeanInstance == null ) {
                System.out.println("SomeOtherThing createOverrideInstance: " + existingBeanInstance);
                return new SomeOtherThing();
            }

            return existingBeanInstance;
        }
    }


    @Target({ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @BeanOverride(CustomBeanOverrideProcessor.class)
    public @interface MyCustomBeanOverride {

        Class<?>[] types() default {};

    }

    static class CustomBeanOverrideProcessor implements BeanOverrideProcessor {

        static List<CustomBeanOverrideProcessor> CREATED_INSTANCES = new ArrayList<>();

        static boolean createHandlersCalled = false;

        public CustomBeanOverrideProcessor() {
            CREATED_INSTANCES.add(this);
        }

        @Override
        public BeanOverrideHandler createHandler(Annotation overrideAnnotation, Class<?> testClass, Field field) {
            throw new IllegalArgumentException("This should've not been called");
        }

        @Override
        public List<BeanOverrideHandler> createHandlers(Annotation overrideAnnotation, Class<?> testClass) {
            if(!(overrideAnnotation instanceof MyCustomBeanOverride myCustomBeanOverride)) {
                throw new IllegalArgumentException("Was not a MyCustomBeanOverride: " + overrideAnnotation.getClass());
            }

            System.out.println("CustomBeanOverrideProcessor class");
            createHandlersCalled = true;
            List<BeanOverrideHandler> handlers = new ArrayList<>();
            for(var type : myCustomBeanOverride.types()){
                handlers.add(new MyBeanOverrideHandler(null, ResolvableType.forClass(type), null, "", BeanOverrideStrategy.WRAP));
            }

            return handlers;
        }
    }

    @Configuration
    @MyCustomBeanOverride(types = {AThing.class})
    static class Config {



    }




    @Test
    void test(@Autowired ApplicationContext applicationContext) {
        var b = applicationContext.getBean(AThing.class);
        JUnitAssertions.assertEquals(1, CustomBeanOverrideProcessor.CREATED_INSTANCES.size());
        JUnitAssertions.assertTrue(CustomBeanOverrideProcessor.createHandlersCalled);
        JUnitAssertions.assertInstanceOf(SomeOtherThing.class, b);

    }

}

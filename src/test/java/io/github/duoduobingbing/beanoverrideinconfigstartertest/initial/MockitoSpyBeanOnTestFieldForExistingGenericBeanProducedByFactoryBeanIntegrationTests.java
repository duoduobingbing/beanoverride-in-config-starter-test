package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.ExampleGenericService;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.SimpleExampleStringGenericService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} on a test class field can be used to replace an existing
 * bean with generics that's produced by a factory bean.
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanOnTestFieldForExistingGenericBeanProducedByFactoryBeanIntegrationTests {

    // gh-40234

    @MockitoSpyBean(name = "exampleService")
    private ExampleGenericService<String> exampleService;

    @Test
    void testSpying() {
        assertThat(Mockito.mockingDetails(this.exampleService).isSpy()).isTrue();
        assertThat(Mockito.mockingDetails(this.exampleService).getMockCreationSettings().getSpiedInstance())
                .isInstanceOf(SimpleExampleStringGenericService.class);
    }

    @Configuration(proxyBeanMethods = false)
    @Import(FactoryBeanRegistrar.class)
    static class SpyBeanOnTestFieldForExistingBeanConfig {

    }

    static class FactoryBeanRegistrar implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            RootBeanDefinition definition = new RootBeanDefinition(ExampleGenericServiceFactoryBean.class);
            definition.setTargetType(
                    ResolvableType.forClassWithGenerics(
                            ExampleGenericServiceFactoryBean.class,
                            null,
                            ExampleGenericService.class
                    )
            );
            registry.registerBeanDefinition("exampleService", definition);
        }

    }

    static class ExampleGenericServiceFactoryBean<T, U extends ExampleGenericService<T>> implements FactoryBean<U> {

        @SuppressWarnings("unchecked")
        @Override
        public U getObject() throws Exception {
            return (U) new SimpleExampleStringGenericService();
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Class<ExampleGenericService> getObjectType() {
            return ExampleGenericService.class;
        }

    }

}

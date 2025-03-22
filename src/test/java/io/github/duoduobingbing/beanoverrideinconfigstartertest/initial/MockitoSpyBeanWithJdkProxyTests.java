package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

/**
 * Tests for {@link org.springframework.test.context.bean.override.mockito.MockitoSpyBean @MockitoSpyBean} with a JDK proxy.
 */
@ExtendWith(SpringExtension.class)
class MockitoSpyBeanWithJdkProxyTests {

    @Autowired
    private ExampleService service;

    @MockitoSpyBean
    private ExampleRepository repository;

    @Test
    void jdkProxyCanBeSpied() {
        Example example = this.service.find("id");
        assertThat(example.id).isEqualTo("id");
        then(this.repository).should().find("id");
    }

    @Configuration(proxyBeanMethods = false)
    @Import(ExampleService.class)
    static class Config {

        @Bean
        ExampleRepository dateService() {
            return (ExampleRepository) Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class<?>[]{ExampleRepository.class},
                    (proxy, method, args) -> new Example((String) args[0])
            );
        }

    }

    static class ExampleService {

        private final ExampleRepository repository;

        ExampleService(ExampleRepository repository) {
            this.repository = repository;
        }

        Example find(String id) {
            return this.repository.find(id);
        }

    }

    interface ExampleRepository {

        Example find(String id);

    }

    static class Example {

        private final String id;

        Example(String id) {
            this.id = id;
        }

    }

}

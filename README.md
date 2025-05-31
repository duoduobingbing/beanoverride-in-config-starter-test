# beanoverride-in-config-starter-test
Spring Boot Test Starter to make it possible again to use `@MockitoBean` in `@Configuration` for tests.

## Background
Since [Spring Boot 3.4](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes#deprecation-of-mockbean-and-spybean) `@MockBean` and `@MockitoSpyBean` became deprecated because support for mocking and spying beans in tests is now directly supported by Spring Framework. The new annotations for this are `@MockitoBean` and `@MocktioSpyBean` and you can now also implement your own annotation that overrides beans in a test context.

| Old Annotation | New Annotation    |
|----------------|-------------------|
| `@MockBean`    | `@MockitoBean`    |
| `@SpyBean`     | `@MockitoSpyBean` |

Sadly, not all features are from Boot are supported by the new annotations. Namely, the following does not work with the new annotations:

Works (old Spring Boot `@MockBean`):
```java
@SpringJUnitConfig
class MyCfgTests() {

    @Autowired
    MyThing myThingMock;

    @Test
    void test() {

    }

    @Configuration
    public static class MyTestConfig {
        @MockBean //This works
        MyThing myThingMock;
    }
}
```

Does not work out of the box (new `@MockitoBean`):
```java
@SpringJUnitConfig
class MyCfgTests() {

    @Autowired
    MyThing myThingMock;

    @Test
    void test() {

    }

    @Configuration
    public static class MyTestConfig {
        @MockitoBean //This does not
        MyThing myThingMock;
    }
}
```

A list of things **not supported** anymore:
- Discovery of these annotations inside `@Configuration` beans: https://github.com/spring-projects/spring-framework/issues/33934
- Directly mocking a `BeanFactory`: https://github.com/spring-projects/spring-framework/issues/34653
- Creating the bean spied on alongside the spy: https://github.com/spring-projects/spring-framework/issues/33935

But since there are scenarios where it is not practical to create a common annotation for sharing mocks/spies between tests, this project
brings back the support for discovery of these annotations on `@Configuration` beans.

## Usage
Just import via
```xml
<dependency>
    <groupId>io.github.duoduobingbing</groupId>
    <artifactId>beanoverride-in-config-starter-test</artifactId>
    <version>[1.0,)</version>
    <scope>test</scope>
</dependency>
```
inside your `pom.xml`.

The dependency defines a new `ContextCustomizerFactory` that is autodiscovered via this dependency's `spring.factories` file.

You can now enjoy working discovery inside your `@Configuration` classes:
```java
@SpringJUnitConfig
class MyCfgTests() {

    @Autowired
    MyThing myThingMock;

    @Test
    void test() {

    }

    @Configuration
    public static class MyTestConfig {
        @MockitoBean
        MyThing myThingMock;
    }
}
```

or in another class like this:

```java
@TestConfiguration
class SomeConfigWithMockitoBean() {

    @MockitoBean
    MyThing myThingMock;
}
```


```java
@SpringJUnitConfig
@Import(SomeConfigWithMockitoBean.class)
class MyCfgTests() {

    @Autowired
    MyThing myThingMock;

    @Test
    void test() {

    }
}
```

Your own custom implementations with e.g. `@BeanOverride(YourCustomBeanOverrideProcessor.class)` will be discovered as well.

# Prerequisites

Minimum Java Version: 21 (LTS)

| *beanoverride-in-config* Version | Supported Spring Boot Version(s) |
|----------------------------------|----------------------------------|
| 1.0                              | 3.4.5, 3.5.0                     |

❌️ **Not supported**: Spring Boot ≦ 3.4.4 

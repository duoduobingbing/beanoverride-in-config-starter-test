package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import java.util.Map;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.BootstrapContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate;
import org.springframework.test.context.cache.DefaultContextCache;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Tests for application context caching when using {@link org.springframework.test.context.bean.override.mockito.MockitoBean @MockitoBean}.
 *
 */
@Disabled("Need to have a second look at this")
class MockitoBeanContextCachingTests {

	private final DefaultContextCache contextCache = new DefaultContextCache(2);

	private final DefaultCacheAwareContextLoaderDelegate delegate = new DefaultCacheAwareContextLoaderDelegate(this.contextCache);

	@AfterEach
	@SuppressWarnings("unchecked")
	void clearCache() {
		Map<MergedContextConfiguration, ApplicationContext> contexts = (Map<MergedContextConfiguration, ApplicationContext>) ReflectionTestUtils.getField(this.contextCache, "contextMap");
		for (ApplicationContext context : contexts.values()) {
			if (context instanceof ConfigurableApplicationContext configurableContext) {
				configurableContext.close();
			}
		}
		this.contextCache.clear();
	}

	@Test
	void whenThereIsANormalBeanAndAMockBeanThenTwoContextsAreCreated() {
		bootstrapContext(TestClass.class);
		Assertions.AssertJAssertions.assertThat(this.contextCache.size()).isOne();
		bootstrapContext(MockedBeanTestClass.class);
		Assertions.AssertJAssertions.assertThat(this.contextCache.size()).isEqualTo(2);
	}

	@Test
	void whenThereIsTheSameMockedBeanInEachTestClassThenOneContextIsCreated() {
		bootstrapContext(MockedBeanTestClass.class);
		Assertions.AssertJAssertions.assertThat(this.contextCache.size()).isOne();
		bootstrapContext(AnotherMockedBeanTestClass.class);
		Assertions.AssertJAssertions.assertThat(this.contextCache.size()).isOne();
	}

	@SuppressWarnings("rawtypes")
	private void bootstrapContext(Class<?> testClass) {
		SpringBootTestContextBootstrapper bootstrapper = new SpringBootTestContextBootstrapper();
		BootstrapContext bootstrapContext = Mockito.mock(BootstrapContext.class);
		BDDMockito.given((Class) bootstrapContext.getTestClass()).willReturn(testClass);
		bootstrapper.setBootstrapContext(bootstrapContext);
		BDDMockito.given(bootstrapContext.getCacheAwareContextLoaderDelegate()).willReturn(this.delegate);
		TestContext testContext = bootstrapper.buildTestContext();
		testContext.getApplicationContext();
	}

	@SpringBootTest(classes = TestConfiguration.class)
	static class TestClass {

	}

	@SpringBootTest(classes = TestConfiguration.class)
	static class MockedBeanTestClass {

		@MockitoBean
		private TestBean testBean;

	}

	@SpringBootTest(classes = TestConfiguration.class)
	static class AnotherMockedBeanTestClass {

		@MockitoBean
		private TestBean testBean;

	}

	@Configuration
	static class TestConfiguration {

		@Bean
		TestBean testBean() {
			return new TestBean();
		}

	}

	static class TestBean {

	}

}

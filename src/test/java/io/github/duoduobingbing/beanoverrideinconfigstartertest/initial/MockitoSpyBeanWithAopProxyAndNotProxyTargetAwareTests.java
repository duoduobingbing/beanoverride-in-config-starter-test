package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;

/**
 * Test {@link MockitoSpyBean @MockitoSpyBean} when mixed with Spring AOP.
 * TODO: adjust: https://github.com/spring-projects/spring-framework/blob/main/spring-test/src/test/java/org/springframework/test/context/bean/override/mockito/integration/MockitoSpyBeanAndSpringAopProxyIntegrationTests.java
 *
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/5837">5837</a>
 */
@ExtendWith(SpringExtension.class)
@Disabled("Have another look at this")
class MockitoSpyBeanWithAopProxyAndNotProxyTargetAwareTests {

	@MockitoSpyBean
	private DateService dateService;

	@Test
	void verifyShouldUseProxyTarget() {
		this.dateService.getDate(false);
		then(this.dateService).should().getDate(false);
		assertThatExceptionOfType(UnfinishedVerificationException.class).isThrownBy(() -> reset(this.dateService));
	}

	@Configuration(proxyBeanMethods = false)
	@EnableCaching(proxyTargetClass = true)
	@Import(DateService.class)
	static class Config {

		@Bean
		CacheResolver cacheResolver(CacheManager cacheManager) {
			SimpleCacheResolver resolver = new SimpleCacheResolver(cacheManager);
			return resolver;
		}

		@Bean
		ConcurrentMapCacheManager cacheManager() {
			ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("test");
			return cacheManager;
		}

	}

	@Service
	public static class DateService {

		@Cacheable(cacheNames = "test")
		public Long getDate(boolean arg) {
			return System.nanoTime();
		}

	}

}

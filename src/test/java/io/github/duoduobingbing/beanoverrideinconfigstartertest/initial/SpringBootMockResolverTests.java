package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import org.junit.jupiter.api.Test;

import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.test.context.bean.override.mockito.SpringMockResolver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringMockResolver}.
 *
 */
class SpringBootMockResolverTests {

	@Test
	void testStaticTarget() {
		MyServiceImpl myService = new MyServiceImpl();
		MyService proxy = ProxyFactory.getProxy(MyService.class, new SingletonTargetSource(myService));
		Object target = new SpringMockResolver().resolve(proxy);
		assertThat(target).isInstanceOf(MyServiceImpl.class);
	}

	@Test
	void testNonStaticTarget() {
		MyServiceImpl myService = new MyServiceImpl();
		MyService proxy = ProxyFactory.getProxy(MyService.class, new HotSwappableTargetSource(myService));
		Object target = new SpringMockResolver().resolve(proxy);
		assertThat(target).isInstanceOf(SpringProxy.class);
	}

	private interface MyService {

		int a();

	}

	private static final class MyServiceImpl implements MyService {

		@Override
		public int a() {
			return 1;
		}

	}

}

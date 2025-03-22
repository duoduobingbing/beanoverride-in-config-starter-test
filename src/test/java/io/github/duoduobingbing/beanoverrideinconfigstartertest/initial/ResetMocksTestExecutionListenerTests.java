package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example.IExampleService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ResetMocksTestExecutionListener}.
 */
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ResetMocksTestExecutionListenerTests {

	@Configuration
	public static class TestConfig {
		@Bean
		MockitoBeans mockBeans() {
			return new MockitoBeans();
		}
	}

	public static class MockitoBeans implements Iterable<Object> {

		private final List<Object> beans = new ArrayList<>();

		void add(Object bean) {
			this.beans.add(bean);
		}

		@Override
		public Iterator<Object> iterator() {
			return this.beans.iterator();
		}

	}

	@Autowired
	private ApplicationContext context;

	@MockitoSpyBean
	ToSpy spied;

	@Test
	void test001() {
		given(getMock("none").greeting()).willReturn("none");
		given(getMock("before").greeting()).willReturn("before");
		given(getMock("after").greeting()).willReturn("after");
		given(getMock("fromFactoryBean").greeting()).willReturn("fromFactoryBean");
		assertThat(this.context.getBean(NonSingletonFactoryBean.class).getObjectInvocations).isEqualTo(0);
		given(this.spied.action()).willReturn("spied");
	}

	@Test
	void test002() {
		assertThat(getMock("none").greeting()).isEqualTo("none");
		assertThat(getMock("before").greeting()).isNull();
		assertThat(getMock("after").greeting()).isNull();
		assertThat(getMock("fromFactoryBean").greeting()).isNull();
		assertThat(this.context.getBean(NonSingletonFactoryBean.class).getObjectInvocations).isEqualTo(0);
		assertThat(this.spied.action()).isNull();
	}

	IExampleService getMock(String name) {
		return this.context.getBean(name, IExampleService.class);
	}

	@Configuration(proxyBeanMethods = false)
	static class Config {

		@Bean
		IExampleService before(MockitoBeans mockedBeans) {
			IExampleService mock = mock(IExampleService.class, MockReset.before());
			mockedBeans.add(mock);
			return mock;
		}

		@Bean
		IExampleService after(MockitoBeans mockedBeans) {
			IExampleService mock = mock(IExampleService.class, MockReset.after());
			mockedBeans.add(mock);
			return mock;
		}

		@Bean
		IExampleService none(MockitoBeans mockedBeans) {
			IExampleService mock = mock(IExampleService.class);
			mockedBeans.add(mock);
			return mock;
		}

		@Bean
		@Lazy
		IExampleService fail() {
			// gh-5870
			throw new RuntimeException();
		}

		@Bean
		BrokenFactoryBean brokenFactoryBean() {
			// gh-7270
			return new BrokenFactoryBean();
		}

		@Bean
		WorkingFactoryBean fromFactoryBean() {
			return new WorkingFactoryBean();
		}

		@Bean
		NonSingletonFactoryBean nonSingletonFactoryBean() {
			return new NonSingletonFactoryBean();
		}

		@Bean
		ToSpyFactoryBean toSpyFactoryBean() {
			return new ToSpyFactoryBean();
		}

	}

	static class BrokenFactoryBean implements FactoryBean<String> {

		@Override
		public String getObject() {
			throw new IllegalStateException();
		}

		@Override
		public Class<?> getObjectType() {
			return String.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}

	}

	static class WorkingFactoryBean implements FactoryBean<IExampleService> {

		private final IExampleService service = mock(IExampleService.class, MockReset.before());

		@Override
		public IExampleService getObject() {
			return this.service;
		}

		@Override
		public Class<?> getObjectType() {
			return IExampleService.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}

	}

	static class ToSpy {

		String action() {
			return null;
		}

	}

	static class NonSingletonFactoryBean implements FactoryBean<IExampleService> {

		private int getObjectInvocations = 0;

		@Override
		public IExampleService getObject() {
			this.getObjectInvocations++;
			return mock(IExampleService.class, MockReset.before());
		}

		@Override
		public Class<?> getObjectType() {
			return IExampleService.class;
		}

		@Override
		public boolean isSingleton() {
			return false;
		}

	}

	static class ToSpyFactoryBean implements FactoryBean<ToSpy> {

		@Override
		public ToSpy getObject() throws Exception {
			return new ToSpy();
		}

		@Override
		public Class<?> getObjectType() {
			return ToSpy.class;
		}

	}

}

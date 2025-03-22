package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Tests for {@link MockitoBean} with abstract class and generics.
 *
 * @param <T> type of thing
 * @param <U> type of something
 */
@SpringBootTest(classes = AbstractMockitoBeanOnGenericTests.TestConfiguration.class)
abstract class AbstractMockitoBeanOnGenericTests<T extends AbstractMockitoBeanOnGenericTests.Thing<U>, U extends AbstractMockitoBeanOnGenericTests.Something> {

	@Autowired
	private T thing;

	@MockitoBean
	private U something;

	@Test
	void mockBeanShouldResolveConcreteType() {
		Assertions.AssertJAssertions.assertThat(this.something).isInstanceOf(SomethingImpl.class);
	}

	abstract static class Thing<T extends AbstractMockitoBeanOnGenericTests.Something> {

		@Autowired
		private T something;

		T getSomething() {
			return this.something;
		}

		void setSomething(T something) {
			this.something = something;
		}

	}

	static class SomethingImpl extends Something {

	}

	static class ThingImpl extends Thing<SomethingImpl> {

	}

	static class Something {

	}

	@Configuration
	static class TestConfiguration {

		@Bean
		ThingImpl thing() {
			return new ThingImpl();
		}

	}

}

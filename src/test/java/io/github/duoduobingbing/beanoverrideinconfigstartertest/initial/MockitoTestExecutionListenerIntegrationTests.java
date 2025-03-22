

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Integration tests for {@link MockitoTestExecutionListener}.
 *
 */
@ExtendWith(SpringExtension.class)
class MockitoTestExecutionListenerIntegrationTests {

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class MockedStaticTests {

		private static final UUID uuid = UUID.randomUUID();

		@Mock
		private MockedStatic<UUID> mockedStatic;

		@Test
		@Order(1)
		@Disabled
		void shouldReturnConstantValueDisabled() {
			this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
			UUID result = UUID.randomUUID();
			assertThat(result).isEqualTo(uuid);
		}

		@Test
		@Order(2)
		void shouldNotFailBecauseOfMockedStaticNotBeingClosed() {
			this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
			UUID result = UUID.randomUUID();
			assertThat(result).isEqualTo(uuid);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
	class MockedStaticTestsDirtiesContext {

		private static final UUID uuid = UUID.randomUUID();

		@Mock
		private MockedStatic<UUID> mockedStatic;

		@Test
		@Order(1)
		@Disabled
		void shouldReturnConstantValueDisabled() {
			this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
			UUID result = UUID.randomUUID();
			assertThat(result).isEqualTo(uuid);
		}

		@Test
		@Order(2)
		void shouldNotFailBecauseOfMockedStaticNotBeingClosed() {
			this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
			UUID result = UUID.randomUUID();
			assertThat(result).isEqualTo(uuid);
		}

		@Test
		@Order(3)
		void shouldNotFailBecauseOfMockedStaticNotBeingClosedWhenMocksAreReinjected() {
			this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
			UUID result = UUID.randomUUID();
			assertThat(result).isEqualTo(uuid);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestClassOrder(ClassOrderer.OrderAnnotation.class)
	class MockedStaticTestsIfClassContainsOnlyDisabledTests {

		@Nested
		@Order(1)
		class TestClass1 {

			private static final UUID uuid = UUID.randomUUID();

			@Mock
			private MockedStatic<UUID> mockedStatic;

			@Test
			@Order(1)
			@Disabled
			void disabledTest() {
				this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
			}

		}

		@Nested
		@Order(2)
		class TestClass2 {

			private static final UUID uuid = UUID.randomUUID();

			@Mock
			private MockedStatic<UUID> mockedStatic;

			@Test
			@Order(1)
			void shouldNotFailBecauseMockedStaticHasNotBeenClosed() {
				this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
				UUID result = UUID.randomUUID();
				assertThat(result).isEqualTo(uuid);
			}

		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestClassOrder(ClassOrderer.OrderAnnotation.class)
	class MockedStaticTestsIfClassContainsNoTests {

		@Nested
		@Order(1)
		class TestClass1 {

			@Mock
			private MockedStatic<UUID> mockedStatic;

		}

		@Nested
		@Order(2)
		class TestClass2 {

			private static final UUID uuid = UUID.randomUUID();

			@Mock
			private MockedStatic<UUID> mockedStatic;

			@Test
			@Order(1)
			void shouldNotFailBecauseMockedStaticHasNotBeenClosed() {
				this.mockedStatic.when(UUID::randomUUID).thenReturn(uuid);
				UUID result = UUID.randomUUID();
				assertThat(result).isEqualTo(uuid);
			}

		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class ConfigureMockInBeforeEach {

		@Mock
		private List<String> mock;

		@BeforeEach
		void setUp() {
			given(this.mock.size()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldUseSetUpConfiguration() {
			assertThat(this.mock.size()).isEqualTo(1);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.size()).willReturn(2);
			assertThat(this.mock.size()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldNotBeAffectedByOtherTests() {
			assertThat(this.mock.size()).isEqualTo(1);
		}

	}


	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@Import(MyBeanConfiguration.class)
	class ConfigureMockBeanWithResetAfterInBeforeEach {

		@MockitoBean(reset = MockReset.AFTER)
		private MyBean mock;

		@BeforeEach
		void setUp() {
			given(this.mock.call()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldUseSetUpConfiguration() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.call()).willReturn(2);
			assertThat(this.mock.call()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldNotBeAffectedByOtherTests() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@Import(MyBeanConfiguration.class)
	class ConfigureMockBeanWithResetBeforeInBeforeEach {

		@MockitoBean(reset = MockReset.BEFORE)
		private MyBean mock;

		@BeforeEach
		void setUp() {
			given(this.mock.call()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldUseSetUpConfiguration() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.call()).willReturn(2);
			assertThat(this.mock.call()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldNotBeAffectedByOtherTests() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@Import(MyBeanConfiguration.class)
	class ConfigureMockBeanWithResetNoneInBeforeEach {

		@MockitoBean(reset = MockReset.NONE)
		private MyBean mock;

		@BeforeEach
		void setUp() {
			given(this.mock.call()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldUseSetUpConfiguration() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.call()).willReturn(2);
			assertThat(this.mock.call()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldNotBeAffectedByOtherTests() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(Lifecycle.PER_CLASS)
	@Import(MyBeanConfiguration.class)
	class ConfigureMockBeanWithResetAfterInBeforeAll {

		@MockitoBean(reset = MockReset.AFTER)
		private MyBean mock;

		@BeforeAll
		void setUp() {
			given(this.mock.call()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldUseSetUpConfiguration() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.call()).willReturn(2);
			assertThat(this.mock.call()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldResetMockAfterReconfiguration() {
			assertThat(this.mock.call()).isEqualTo(0);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(Lifecycle.PER_CLASS)
	@Import(MyBeanConfiguration.class)
	class ConfigureMockBeanWithResetBeforeInBeforeAll {

		@MockitoBean(reset = MockReset.BEFORE)
		private MyBean mock;

		@BeforeAll
		void setUp() {
			given(this.mock.call()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldResetMockBeforeThisMethod() {
			assertThat(this.mock.call()).isEqualTo(0);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.call()).willReturn(2);
			assertThat(this.mock.call()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldResetMockAfterReconfiguration() {
			assertThat(this.mock.call()).isEqualTo(0);
		}

	}

	@Nested
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	@TestInstance(Lifecycle.PER_CLASS)
	@Import(MyBeanConfiguration.class)
	class ConfigureMockBeanWithResetNoneInBeforeAll {

		@MockitoBean(reset = MockReset.NONE)
		private MyBean mock;

		@BeforeAll
		void setUp() {
			given(this.mock.call()).willReturn(1);
		}

		@Test
		@Order(1)
		void shouldUseSetUpConfiguration() {
			assertThat(this.mock.call()).isEqualTo(1);
		}

		@Test
		@Order(2)
		void shouldBeAbleToReconfigureMock() {
			given(this.mock.call()).willReturn(2);
			assertThat(this.mock.call()).isEqualTo(2);
		}

		@Test
		@Order(3)
		void shouldNotResetMock() {
			assertThat(this.mock.call()).isEqualTo(2);
		}

	}

	interface MyBean {

		int call();

	}

	private static final class DefaultMyBean implements MyBean {

		@Override
		public int call() {
			return -1;
		}

	}

	@TestConfiguration(proxyBeanMethods = false)
	private static final class MyBeanConfiguration {

		@Bean
		MyBean myBean() {
			return new DefaultMyBean();
		}

	}

}

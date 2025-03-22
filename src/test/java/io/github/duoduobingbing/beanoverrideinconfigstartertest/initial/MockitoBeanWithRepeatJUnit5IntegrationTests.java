package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial;



import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MockitoBean} and {@link org.junit.jupiter.api.RepeatedTest}.
 *
 * @see <a href="https://github.com/spring-projects/spring-boot/issues/27693">gh-27693</a>
 */
@ExtendWith(SpringExtension.class)
public class MockitoBeanWithRepeatJUnit5IntegrationTests {


	@MockitoBean
	private FirstService first;

	private static int invocations;

	@AfterAll
	public static void afterClass() {
		assertThat(invocations).isEqualTo(2);
	}

	@RepeatedTest(2)
	public void repeatedTest() {
		invocations++;
	}

	interface FirstService {

		String greeting();

	}

}

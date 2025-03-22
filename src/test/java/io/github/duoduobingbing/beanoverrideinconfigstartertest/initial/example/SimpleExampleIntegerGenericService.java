package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;

/**
 * Example generic service implementation for spy tests.
 *
 */
public class SimpleExampleIntegerGenericService implements ExampleGenericService<Integer> {

	@Override
	public Integer greeting() {
		return 123;
	}

}

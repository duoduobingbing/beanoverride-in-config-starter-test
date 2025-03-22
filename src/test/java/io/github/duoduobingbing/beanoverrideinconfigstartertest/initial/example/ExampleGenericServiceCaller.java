package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;

/**
 * Example bean for mocking tests that calls {@link ExampleGenericService}.
 */
public class ExampleGenericServiceCaller {

	private final ExampleGenericService<Integer> integerService;

	private final ExampleGenericService<String> stringService;

	public ExampleGenericServiceCaller(ExampleGenericService<Integer> integerService, ExampleGenericService<String> stringService) {
		this.integerService = integerService;
		this.stringService = stringService;
	}

	public ExampleGenericService<Integer> getIntegerService() {
		return this.integerService;
	}

	public ExampleGenericService<String> getStringService() {
		return this.stringService;
	}

	public String sayGreeting() {
		return "I say " + this.integerService.greeting() + " " + this.stringService.greeting();
	}

}

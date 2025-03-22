package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;

/**
 * An {@link IExampleService} that uses a custom qualifier.
 *
 */
@MyCustomQualifier
public class CustomQualifierExampleService implements IExampleService {

	@Override
	public String greeting() {
		return "ACustomQualifier greeting";
	}

}

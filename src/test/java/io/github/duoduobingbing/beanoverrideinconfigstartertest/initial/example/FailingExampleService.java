package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;

public class FailingExampleService implements IExampleService {

    @Override
    public String greeting() {
        throw new IllegalStateException("Failed");
    }

}

package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;

public class RealExampleService implements IExampleService {

    private final String greeting;

    public RealExampleService(String greeting) {
        this.greeting = greeting;
    }

    @Override
    public String greeting() {
        return this.greeting;
    }

}
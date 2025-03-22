package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;


public class ExampleServiceCaller {

    private final IExampleService service;

    public ExampleServiceCaller(IExampleService service) {
        this.service = service;
    }

    public IExampleService getService() {
        return this.service;
    }

    public String sayGreeting() {
        return "I say " + this.service.greeting();
    }

}

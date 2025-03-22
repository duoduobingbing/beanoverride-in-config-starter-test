package io.github.duoduobingbing.beanoverrideinconfigstartertest.initial.example;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A custom qualifier for testing.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCustomQualifier {

}

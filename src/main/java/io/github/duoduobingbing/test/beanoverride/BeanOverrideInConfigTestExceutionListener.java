package io.github.duoduobingbing.test.beanoverride;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;

import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.bean.override.BeanOverrideTestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * This {@link TestExecutionListener TestExecutionListener} is currently only used for debug purposes and prints whether {@link DependencyInjectionTestExecutionListener#REINJECT_DEPENDENCIES_ATTRIBUTE REINJECT_DEPENDENCIES_ATTRIBUTE}  was enabled or not.
 * Runs after {@link BeanOverrideTestExecutionListener BeanOverrideTestExecutionListener}.
 */
public class BeanOverrideInConfigTestExceutionListener extends AbstractTestExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(BeanOverrideInConfigTestExceutionListener.class);


    @Override
    public int getOrder() {
        return BeanOverrideTestExecutionListener.ORDER + 1; //Run after BeanOverrideTestExecutionListener
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        Object reinjectDependenciesAttribute = testContext.getAttribute(DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE);
        logger.debug("[BOINC] ReinjectDependenciesAttribute: {}", Boolean.TRUE.equals(reinjectDependenciesAttribute));
    }





}
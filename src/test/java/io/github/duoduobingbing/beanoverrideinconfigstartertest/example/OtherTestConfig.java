package io.github.duoduobingbing.beanoverrideinconfigstartertest.example;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.MockBeanOld;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
public class OtherTestConfig {

    @MockBeanOld(name = "mockBeanInDifferentConfig")
    AThing aThing;

    @MockitoBean(name = "mockitoBeanInDifferentConfig")
    AThing aThing2;

}

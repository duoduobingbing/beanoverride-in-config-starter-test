package io.github.duoduobingbing.beanoverrideinconfigstartertest;


import io.github.duoduobingbing.beanoverrideinconfigstartertest.example.AThing;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions.AssertJAssertions;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions.JUnitAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SupportMockitoBeanOnConfigurationLevelTest {

    @Test
    void test(@Autowired ApplicationContext applicationContext) {
        AThing foundBean = applicationContext.getBean(AThing.class);

        AssertJAssertions.assertThat(foundBean).isNotNull();
        Object mock = JUnitAssertions.assertDoesNotThrow(() -> Mockito.mockingDetails(foundBean).getMock());
        AssertJAssertions.assertThat(foundBean).isSameAs(mock);

    }

    @Configuration
    @MockitoBean(types = AThing.class)
    public static class MyConfiguration {}

}

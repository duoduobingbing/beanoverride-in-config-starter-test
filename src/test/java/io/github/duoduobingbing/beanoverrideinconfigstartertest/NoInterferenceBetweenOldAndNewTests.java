package io.github.duoduobingbing.beanoverrideinconfigstartertest;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.example.AThing;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.example.OtherTestConfig;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions.AssertJAssertions;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions.JUnitAssertions;
import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.MockBeanOld;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Map;
import java.util.Set;

@SpringJUnitConfig
@ExtendWith({SpringExtension.class})
@Import({NoInterferenceBetweenOldAndNewTests.TestConfig.class, OtherTestConfig.class})
class NoInterferenceBetweenOldAndNewTests {


    @MockitoBean(name = "mockitoBeanPlain")
    public AThing aThing;

    @MockBeanOld(name = "mockBeanPlain")
    public AThing aThing2;

    @TestConfiguration
    public static class TestConfig {
        @MockitoBean(name = "mockitoBeanInDirectConfig")
        public AThing aThingX;

        @MockBeanOld(name = "mockBeanInDirectConfig")
        public AThing aThing2;

    }


    @Test
    void contextLoads(@Autowired Map<String, AThing> aThingList) {
        System.out.println(aThingList.size());
        System.out.println(String.join(", ", aThingList.keySet()));

        JUnitAssertions.assertEquals(6, aThingList.size());
        AssertJAssertions.assertThat(aThingList.keySet()).containsExactlyInAnyOrderElementsOf(Set.of(
                "mockitoBeanPlain",
                "mockBeanPlain",
                "mockitoBeanInDirectConfig",
                "mockBeanInDirectConfig",
                "mockitoBeanInDifferentConfig",
                "mockBeanInDifferentConfig"
        ));

        AssertJAssertions.assertThat(aThingList.values()).allSatisfy(aThing -> {
                    JUnitAssertions.assertDoesNotThrow(() -> Mockito.mockingDetails(aThing));
                }
        );
    }

}


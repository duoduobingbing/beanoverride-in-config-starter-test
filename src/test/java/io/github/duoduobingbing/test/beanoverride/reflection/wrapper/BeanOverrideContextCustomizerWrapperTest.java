package io.github.duoduobingbing.test.beanoverride.reflection.wrapper;

import io.github.duoduobingbing.beanoverrideinconfigstartertest.test.helper.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.bean.override.BeanOverrideHandler;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BeanOverrideContextCustomizerWrapperTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testConstructor() throws Exception {
        Class<?> clazz = Class.forName("org.springframework.test.context.bean.override.BeanOverrideContextCustomizer");
        Set<BeanOverrideHandler> handlerSet = Set.of();
        Constructor<? extends ContextCustomizer> ctr = (Constructor<? extends ContextCustomizer>) clazz.getDeclaredConstructor(Set.class);
        ctr.setAccessible(true);

        ContextCustomizer instance = ctr.newInstance(handlerSet);

        BeanOverrideContextCustomizerWrapper wrapper = new BeanOverrideContextCustomizerWrapper(instance);
        ContextCustomizer rs = wrapper.get();

        Assertions.AssertJAssertions.assertThat(rs).isNotNull();
    }

    @Test
    public void testFields() {
        Assertions.AssertJAssertions.assertThat(BeanOverrideContextCustomizerWrapper.INFRASTRUCTURE_BEAN_NAME).isNotNull();
        Assertions.AssertJAssertions.assertThat(BeanOverrideContextCustomizerWrapper.EARLY_INFRASTRUCTURE_BEAN_NAME).isNotNull();
    }

}
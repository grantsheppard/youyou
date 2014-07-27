package com.googlecode.youyou.test;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

public class FieldValueRandomiserFactoryTest {
    private TypeDefaultFieldValueRandomiserFactory factory = new TypeDefaultFieldValueRandomiserFactory();

    @Test
    public void stringTypeRandomiserCanBeCreated() {
        FieldValueRandomiser<Class<String>> randomiser = factory.createFor(String.class);

        assertThat(randomiser, instanceOf(StringFieldValueRandomiser.class));
    }
}
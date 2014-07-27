package com.googlecode.youyou.test;

import com.googlecode.youyou.test.StringFieldValueRandomiser;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringFieldValueRandomiserTest {
    private StringFieldValueRandomiser randomiser;

    @Test
    public void generatesNonNullValue() {
        assertThat(randomiser.nextRandom(), is(notNullValue()));
    }

    @Test
    public void generatesRandomStrings() {
        assertThat(randomiser.nextRandom(), is(not(equalTo(randomiser.nextRandom()))));
    }

    @BeforeClass
    public void setupFixture() {
        randomiser = new StringFieldValueRandomiser();
    }
}

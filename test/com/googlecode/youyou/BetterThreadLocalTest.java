package com.googlecode.youyou;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.asList;
import static com.google.common.collect.Lists.newArrayList;
import static com.googlecode.youyou.BetterThreadLocal.InitialValueFactory;
import static com.googlecode.youyou.BetterThreadLocal.threadLocal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BetterThreadLocalTest {

    private final FoobarInitialValueFactory initialValueFactory = new FoobarInitialValueFactory();

    @Test
    public void twoThreadsResultsInTwoValuesBeingCreated() throws Exception {
        Foobar foobar = threadLocal(Foobar.class, initialValueFactory);

        executeInNewThread(foobar, foobar);

        assertThat(initialValueFactory.invocationCount, is(equalTo(2)));
    }

    @Test
    public void singleThreadResultsInOneValueBeingCreated() throws Exception {
        Foobar foobar = threadLocal(Foobar.class, initialValueFactory);

        foobar.testMethod();
        foobar.testMethod();

        assertThat(initialValueFactory.invocationCount, is(equalTo(1)));
    }

    private void executeInNewThread(final Foobar firstFoobar, final Foobar... otherFoobars) throws InterruptedException {
        List<Foobar> foobars = asList(firstFoobar, otherFoobars);
        for (Foobar foobar : foobars) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    firstFoobar.testMethod();
                }
            });
            thread.start();
            thread.join();
        }
    }

    @Before
    public void resetFixture() {
        initialValueFactory.invocationCount = 0;
    }

    private interface Foobar {
        void testMethod();
    }

    private class FoobarInitialValueFactory implements InitialValueFactory<Foobar> {
        public int invocationCount;

        @Override
        public Foobar create() {
            invocationCount++;
            return new Foobar() {
                @Override
                public void testMethod() {
                }
            };
        }
    }
}
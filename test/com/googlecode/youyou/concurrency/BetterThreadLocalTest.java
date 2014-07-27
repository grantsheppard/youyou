package com.googlecode.youyou.concurrency;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.google.common.collect.Lists.asList;
import static com.googlecode.youyou.concurrency.BetterThreadLocal.InitialValueFactory;
import static com.googlecode.youyou.concurrency.BetterThreadLocal.threadLocal;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class BetterThreadLocalTest {

    private final TestRunnableInitialValueFactory initialValueFactory = new TestRunnableInitialValueFactory();

    @Test
    public void oneThreadLocalIsInitialisedPerThread() throws Exception {
        TestRunnable test = threadLocal(TestRunnable.class, initialValueFactory);

        executeInNewThread(test, test, test);

        assertThat(initialValueFactory.invocationCount, is(equalTo(3)));
    }

    @Test
    public void oneThreadLocalIsInitialisedForSingleThread() throws Exception {
        TestRunnable test = threadLocal(TestRunnable.class, initialValueFactory);

        test.testMethod();
        test.testMethod();

        assertThat(initialValueFactory.invocationCount, is(equalTo(1)));
    }

    private void executeInNewThread(TestRunnable firstTestRunnable,
                                    TestRunnable... otherTestRunnables) throws InterruptedException {
        List<TestRunnable> testRunnables = asList(firstTestRunnable, otherTestRunnables);
        for (final TestRunnable testRunnable : testRunnables) {
            Thread thread = new Thread(testRunnable);
            thread.start();
            thread.join();
        }
    }

    @BeforeMethod
    public void resetFixture() {
        initialValueFactory.invocationCount = 0;
    }

    private interface TestRunnable extends Runnable {
        void testMethod();
    }

    private class DefaultTestRunnable implements TestRunnable {
        @Override
        public void run() {
            testMethod();
        }

        @Override
        public void testMethod() {
        }
    }

    private class TestRunnableInitialValueFactory implements InitialValueFactory<TestRunnable> {
        public int invocationCount;

        @Override
        public TestRunnable create() {
            invocationCount++;
            return new DefaultTestRunnable();
        }
    }
}
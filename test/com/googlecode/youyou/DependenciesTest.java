package com.googlecode.youyou;

import org.junit.Test;

import static com.googlecode.youyou.Dependencies.checkForMissingDependencies;

public class DependenciesTest {
    @Test(expected = IllegalArgumentException.class)
    public void failWhenOnlyDependencyIsNull() {
        checkForMissingDependencies(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failWhenFirstDependencyIsNullAndSecondIsNot() {
        checkForMissingDependencies(null, "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failWhenFirstDependencyIsNotNullAndSecondIs() {
        checkForMissingDependencies("test", (Object) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failWhenAllDependenciesAreNull() {
        checkForMissingDependencies(null, (Object) null);
    }
}

package com.googlecode.youyou.dependency;

import org.testng.annotations.Test;

import static com.googlecode.youyou.dependency.Dependencies.checkForMissingDependencies;

public class DependenciesTest {
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void failWhenOnlyDependencyIsNull() {
        checkForMissingDependencies(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void failWhenFirstDependencyIsNullAndSecondIsNot() {
        checkForMissingDependencies(null, "test");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void failWhenFirstDependencyIsNotNullAndSecondIs() {
        checkForMissingDependencies("test", (Object) null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void failWhenAllDependenciesAreNull() {
        checkForMissingDependencies(null, (Object) null);
    }
}

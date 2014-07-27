package com.googlecode.youyou.dependency;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.asList;
import static java.lang.String.format;

public class Dependencies {
    public static void checkForMissingDependencies(Object first, Object... others) {
        List<Object> dependencies = asList(first, others);
        for (int i = 0; i < dependencies.size(); i++)
            checkArgument(dependencies.get(i) != null, format("required dependency is missing (index %d)", i));
    }
}

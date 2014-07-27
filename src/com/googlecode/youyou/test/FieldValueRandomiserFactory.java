package com.googlecode.youyou.test;

public interface FieldValueRandomiserFactory {
    <T> FieldValueRandomiser<T> createFor(T type);
}

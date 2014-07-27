package com.googlecode.youyou.test;

/**(
 * Randomly generate a new value of the specified type.
 */
public interface FieldValueRandomiser<T> {
    T nextRandom();
}

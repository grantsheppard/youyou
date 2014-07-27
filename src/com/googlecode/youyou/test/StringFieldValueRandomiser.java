package com.googlecode.youyou.test;

import java.util.Random;

import static com.google.common.io.BaseEncoding.base64Url;

public class StringFieldValueRandomiser implements FieldValueRandomiser<String> {
    private final Random random = new Random();

    @Override
    public String nextRandom() {
        byte[] buffer = new byte[5];
        random.nextBytes(buffer);
        return base64Url().omitPadding().encode(buffer);
    }
}

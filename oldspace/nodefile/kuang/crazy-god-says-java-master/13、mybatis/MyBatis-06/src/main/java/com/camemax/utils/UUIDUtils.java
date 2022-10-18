package com.camemax.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String createUUID() {
        String createUUID = UUID.randomUUID().toString().replaceAll("-", "");
        return createUUID;
    }
}

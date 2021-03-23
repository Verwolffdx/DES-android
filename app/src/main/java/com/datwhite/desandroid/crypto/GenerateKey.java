package com.datwhite.desandroid.crypto;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class GenerateKey {
    private static String key;

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        GenerateKey.key = key;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String generate(int keyLength) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte[] values = new byte[keyLength];
        random.nextBytes(values);

        key = Base64.getEncoder().encodeToString(values).substring(0, keyLength);

        return key;
    }
}

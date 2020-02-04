package com.ncov.module.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OkHttpClientFactory {

    private static final OkHttpClient client = new OkHttpClient();

    public static OkHttpClient getInstance() {
        return client;
    }
}

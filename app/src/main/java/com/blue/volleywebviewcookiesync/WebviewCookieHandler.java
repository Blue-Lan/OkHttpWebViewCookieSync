package com.blue.volleywebviewcookiesync;

/**
 * Created by Blue on 16/7/31.
 */

import android.webkit.CookieManager;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a synchronization point between the webview cookie store and OkHttpClient cookie store
 */
public final class WebviewCookieHandler extends CookieHandler {
    private CookieManager webviewCookieManager = CookieManager.getInstance();

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        String url = uri.toString();
        String cookieValue = this.webviewCookieManager.getCookie(url);

        if (cookieValue==null) {
            cookieValue = "";
        }

        Map<String, List<String>> cookies = new HashMap<>();

        cookies.put("Cookie", Arrays.asList(cookieValue));

        return cookies;
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        String url = uri.toString();

        for(String header : responseHeaders.keySet()) {
            if(header.equalsIgnoreCase("Set-Cookie") || header.equalsIgnoreCase("Set-Cookie2")) {
                for(String value : responseHeaders.get(header)) {
                    this.webviewCookieManager.setCookie(url, value);
                }
            }
        }
    }
}

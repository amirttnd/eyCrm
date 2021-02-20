package com.zaloni.security.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {
    public static void add(String name, String value, HttpServletResponse response) {
        response.addCookie(new Cookie(name, value));
    }
}

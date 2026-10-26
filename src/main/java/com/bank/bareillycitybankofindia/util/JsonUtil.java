package com.bank.bareillycitybankofindia.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static void sendJsonResponse(HttpServletResponse response, int statusCode, boolean success, String message, Object data) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> map = new HashMap<>();
            map.put("success", success);
            map.put("message", message);
            if (data != null) {
                map.put("data", data);
            }

            PrintWriter out = response.getWriter();
            out.print(gson.toJson(map));
            out.flush();
        } catch (Exception e) {
            // Log fallback
        }
    }
}

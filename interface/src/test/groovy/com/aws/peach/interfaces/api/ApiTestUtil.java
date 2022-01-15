package com.aws.peach.interfaces.api;

public class ApiTestUtil {

    private static int orderNo = 0;

    public static String generateNewOrderNo() {
        orderNo += 1;
        return String.valueOf(orderNo);
    }

    public static String url(int port, String suffix) {
        return "http://localhost:" + port + suffix;
    }
}

package com.workable.matchmakers.util;

public class DBLoggingHelper {

    public static String create(Object entity) {
        return "CREATED: " + entity;
    }

    public static String replaced(Object entity) {
        return "REPLACED: " + entity;
    }

    public static String update(Object entity) {
        return "UPDATED: " + entity;
    }

    public static String delete(Object entity) {
        return "DELETE: " + entity;
    }
}


package ru.dinoelnirgihc.constant;

public final class SystemConstant {

    private SystemConstant () {
    }

    public static final String DB_URL = "DB_URL";
    public static final String DB_USER = "DB_USER";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    public static final String ENV_DB_URL = "${DB_URL}";
    public static final String ENV_DB_USER = "${DB_USER}";
    public static final String ENV_DB_PASSWORD = "${DB_PASSWORD}";
}

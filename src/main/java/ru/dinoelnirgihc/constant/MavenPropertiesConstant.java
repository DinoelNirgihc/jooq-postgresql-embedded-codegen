package ru.dinoelnirgihc.constant;

public final class MavenPropertiesConstant {

    private MavenPropertiesConstant() {
    }

    public static final String ENV_MAVEN_PROJECT = "${project}";
    public static final String ENV_DB_CONTEXT = "embedded-postgres";
    public static final String ENV_DB_DRIVER = "EMB_DB_DRIVER";
    public static final String ENV_DB_URL = "EMB_DB_URL";
    public static final String ENV_DB_USER = "EMB_DB_USER";
    public static final String ENV_DB_PASSWORD = "EMB_DB_PASSWORD";
}

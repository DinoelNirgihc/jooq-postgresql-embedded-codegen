package ru.dinoelnirgihc.util;

import org.apache.maven.project.MavenProject;
import ru.dinoelnirgihc.mojo.StartPostgresMojo;

public final class TestUtil {

    private TestUtil() {

    }

    public static MavenProject getProjectFromMojo(StartPostgresMojo mojo) throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = StartPostgresMojo.class.getDeclaredField("project");
        field.setAccessible(true);
        return (MavenProject) field.get(mojo);
    }
}

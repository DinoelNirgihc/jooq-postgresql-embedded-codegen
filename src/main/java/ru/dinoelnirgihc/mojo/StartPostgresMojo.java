package ru.dinoelnirgihc.mojo;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import ru.dinoelnirgihc.constant.DatabaseConstant;
import ru.dinoelnirgihc.constant.SystemConstant;

import java.io.IOException;

@Mojo(name = "start", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class StartPostgresMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "pg.port", defaultValue = "5433")
    private int port;

    @Parameter(property = "pg.db_name", defaultValue = DatabaseConstant.DB_NAME)
    private String dbName;

    @Parameter(property = "pg.user", defaultValue = DatabaseConstant.USER)
    private String user;

    @Parameter(property = "pg.password", defaultValue = DatabaseConstant.PASSWORD)
    private String password;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            EmbeddedPostgres postgres = EmbeddedPostgres.builder()
                    .setPort(port)
                    .setCleanDataDirectory(true)
                    .start();

            project.setContextValue("embedded-postgres", postgres);

            System.setProperty(SystemConstant.DB_URL, String.format("jdbc:postgresql://localhost:%s/%s", port, dbName));
            System.setProperty(SystemConstant.DB_USER, DatabaseConstant.USER);
            System.setProperty(SystemConstant.DB_PASSWORD, DatabaseConstant.PASSWORD);

            Runtime.getRuntime().addShutdownHook(
                    new Thread(
                            () -> {
                                try {
                                    postgres.close();

                                    System.clearProperty(SystemConstant.DB_URL);
                                    System.clearProperty(SystemConstant.DB_USER);
                                    System.clearProperty(SystemConstant.DB_PASSWORD);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

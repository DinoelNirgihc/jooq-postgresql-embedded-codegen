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
import ru.dinoelnirgihc.constant.MavenPropertiesConstant;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Mojo(name = "start", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class StartPostgresMojo extends AbstractMojo {

    @Parameter(defaultValue = MavenPropertiesConstant.ENV_MAVEN_PROJECT, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = DatabaseConstant.DEFAULT_PORT_VALUE)
    private int port;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            final EmbeddedPostgres postgres = configureEmbeddedPostgres();

            addEmbeddedPostgresInMavenContext(postgres);
            addDatabaseEnvironmentInMavenProperties(postgres);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EmbeddedPostgres configureEmbeddedPostgres() throws IOException {
        return EmbeddedPostgres.builder()
                .setPort(port)
                .start();
    }

    private void addEmbeddedPostgresInMavenContext(final EmbeddedPostgres postgres) {
        project.setContextValue(MavenPropertiesConstant.ENV_DB_CONTEXT, postgres);
    }

    private void addDatabaseEnvironmentInMavenProperties(final EmbeddedPostgres postgres) {
        final Properties mavenProperties = project.getProperties();

        mavenProperties.putAll(
                Map.of(
                        MavenPropertiesConstant.ENV_DB_URL, postgres.getJdbcUrl(DatabaseConstant.DEFAULT_USER_VALUE, DatabaseConstant.DEFAULT_DB_NAME_VALUE),
                        MavenPropertiesConstant.ENV_DB_USER, DatabaseConstant.DEFAULT_USER_VALUE,
                        MavenPropertiesConstant.ENV_DB_PASSWORD, DatabaseConstant.DEFAULT_PASSWORD_VALUE,
                        MavenPropertiesConstant.ENV_DB_DRIVER, DatabaseConstant.DRIVER
                )
        );
    }
}

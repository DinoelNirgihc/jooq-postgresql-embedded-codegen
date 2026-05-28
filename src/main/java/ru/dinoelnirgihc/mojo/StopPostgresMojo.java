package ru.dinoelnirgihc.mojo;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import ru.dinoelnirgihc.constant.MavenPropertiesConstant;

import java.io.IOException;

@Mojo(name = "stop", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class StopPostgresMojo extends AbstractMojo {

    @Parameter(defaultValue = MavenPropertiesConstant.ENV_MAVEN_PROJECT, readonly = true)
    private MavenProject project;

    @Override
    public void execute() {
        final EmbeddedPostgres embeddedPostgres =
                (EmbeddedPostgres) project.getContextValue(MavenPropertiesConstant.ENV_DB_CONTEXT);

        try {
            embeddedPostgres.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

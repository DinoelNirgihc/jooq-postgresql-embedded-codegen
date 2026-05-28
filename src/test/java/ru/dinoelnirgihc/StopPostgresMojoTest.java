package ru.dinoelnirgihc;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.dinoelnirgihc.constant.MavenPropertiesConstant;
import ru.dinoelnirgihc.constant.TestConstant;
import ru.dinoelnirgihc.mojo.StartPostgresMojo;
import ru.dinoelnirgihc.mojo.StopPostgresMojo;
import ru.dinoelnirgihc.util.TestUtil;

import java.io.IOException;
import java.net.ServerSocket;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@MojoTest
public class StopPostgresMojoTest {

    private StartPostgresMojo startPostgresMojo;
    private StopPostgresMojo stopPostgresMojo;

    @BeforeEach
    void initializeMojo(
            @InjectMojo(goal = "start")
            @MojoParameter(name = "port", value = TestConstant.DB_PORT)
            StartPostgresMojo inintialStartPostgresMojo,
            @InjectMojo(goal = "stop")
            StopPostgresMojo inintialStopPostgresMojo
    ) {
        this.startPostgresMojo = inintialStartPostgresMojo;
        this.stopPostgresMojo = inintialStopPostgresMojo;
    }

    @Test
    void shouldStopEmbeddedPostgres() throws MojoExecutionException, MojoFailureException {
        //give
        int port = Integer.parseInt(TestConstant.DB_PORT);
        startPostgresMojo.execute();
        //when
        stopPostgresMojo.execute();
        //then
        assertThat(isPortAvailable(port)).isTrue();
    }


    private boolean isPortAvailable(int port) {
        try (final ServerSocket socket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

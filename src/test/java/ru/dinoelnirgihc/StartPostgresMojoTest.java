package ru.dinoelnirgihc;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoParameter;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import ru.dinoelnirgihc.constant.MavenPropertiesConstant;
import ru.dinoelnirgihc.constant.TestConstant;
import ru.dinoelnirgihc.mojo.StartPostgresMojo;
import ru.dinoelnirgihc.util.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;

@MojoTest
class StartPostgresMojoTest {

    @Test
    @InjectMojo(goal = "start")
    @MojoParameter(name = "port", value = TestConstant.DB_PORT)
    void shouldStartEmbeddedPostgresWithCorrectPort(StartPostgresMojo startPostgresMojo)
            throws MojoExecutionException, MojoFailureException, NoSuchFieldException, IllegalAccessException {
        //when
        startPostgresMojo.execute();
        //then
        MavenProject mavenProject = TestUtil.getProjectFromMojo(startPostgresMojo);
        assertThat(mavenProject).isNotNull();
        EmbeddedPostgres embeddedPostgres = (EmbeddedPostgres) mavenProject.getContextValue(MavenPropertiesConstant.ENV_DB_CONTEXT);
        assertThat(embeddedPostgres).isNotNull();
        int port = embeddedPostgres.getPort();
        assertThat(port).isEqualTo(Integer.parseInt(TestConstant.DB_PORT));
    }
}

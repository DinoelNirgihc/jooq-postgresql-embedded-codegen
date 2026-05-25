package ru.dinoelnirgihc.mojo;

import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import ru.dinoelnirgihc.constant.DatabaseConstant;
import ru.dinoelnirgihc.constant.SystemConstant;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

@Mojo(name = "migrate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class LiquibaseMigrateMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "liquibase.changelog", defaultValue = "src/main/resources/db/changelog/db.changelog-root.xml")
    private String changelogFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String resolvedUrl = resolveProperty(SystemConstant.ENV_DB_URL);
        String resolvedUser = resolveProperty(SystemConstant.ENV_DB_USER);
        String resolvedPassword = resolveProperty(SystemConstant.ENV_DB_PASSWORD);
        Connection connection = null;
        Liquibase liquibase = null;

        try {
            File changelog = new File(changelogFile);
            if (!changelog.exists()) {
                throw new MojoExecutionException(
                        "Changelog file not found: " + changelog.getAbsolutePath()
                );
            }
            Class.forName(DatabaseConstant.DB_DRIVER);
            connection = DriverManager.getConnection(resolvedUrl, resolvedUser, resolvedPassword);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            database.setDefaultSchemaName(DatabaseConstant.DB_DEFAULT_SCHEMA);

            CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);

            updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, database);
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changelogFile);

            updateCommand.execute();
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to run Liquibase migrations", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    getLog().warn("Failed to close database connection: " + e.getMessage());
                }
            }
        }

    }

    private String resolveProperty(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            String propName = value.substring(2, value.length() - 1);
            return System.getProperty(propName);
        }
        return value;
    }
}

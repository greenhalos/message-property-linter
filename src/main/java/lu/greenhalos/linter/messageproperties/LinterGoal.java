package lu.greenhalos.linter.messageproperties;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;
import lu.greenhalos.linter.messageproperties.linter.Linter;
import lu.greenhalos.linter.messageproperties.reader.Reader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

import java.util.List;
import java.util.function.Consumer;

import static lu.greenhalos.linter.messageproperties.linter.Linter.validate;


/**
 * A linter for Message properties.
 */
@Mojo(name = "linter")
public class LinterGoal extends AbstractMojo {

    /**
     * The directory where the message properties are located.
     */
    @Parameter(property = "linter.directory", defaultValue = "src/main/resources")
    private String directory;

    /**
     * The prefix of the files names.
     */
    @Parameter(property = "linter.prefix", defaultValue = "messages")
    private String prefix;

    @Parameter(property = "linter.suffix", defaultValue = ".properties")
    private String suffix;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        Config config = new Config.Builder(directory, prefix, suffix, getLog()).build();
        lint(config);
        getLog().info("Finished successful");
    }


    private static void lint(Config config) throws MojoFailureException {

        try {
            Reader reader = new Reader(config);
            MessageProperties read = reader.getMessageProperties();
            List<Linter.MissingMessagePropertyKey> validate = validate(read);

            if (!validate.isEmpty()) {
                validate.forEach(printToLog(config));
                throw new MojoFailureException("There were invalid message properties");
            }
        } catch (IOException e) {
            config.getLog().error("Something went wrong while finding files", e);
        }
    }


    private static Consumer<Linter.MissingMessagePropertyKey> printToLog(Config config) {

        return
            v -> {
            config.getLog()
                .error("Missing language for key \"" + v.getPropertyKey() + "\": expected languages: "
                    + v.getRequestedLanguages()
                    + " but found " + v.getFoundLanguages());
            v.getMissingLanguages().forEach(l ->
                    config.getLog()
                    .error(
                        "\t" + config.getDirectory() + "/" + config.getPrefix() + l.getFileExtension()
                        + config.getSuffix()));
        };
    }
}

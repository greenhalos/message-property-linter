package lu.greenhalos.linter.messageproperties;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;
import lu.greenhalos.linter.messageproperties.formatter.Formatter;
import lu.greenhalos.linter.messageproperties.reader.Reader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import static lu.greenhalos.linter.messageproperties.writer.Writer.writeMessageProperties;


@Mojo(name = "formatter")
public class FormatterGoal extends AbstractMojo {

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

    @Parameter(property = "linter.dryRun", defaultValue = "false")
    private boolean dryRun;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        Config config = new Config.Builder(directory, prefix, suffix, getLog()).setDryRun(dryRun).build();
        format(config);
        getLog().info("Finished successful");
    }


    private static void format(Config config) {

        try {
            Reader reader = new Reader(config);
            MessageProperties messageProperties = reader.getMessageProperties();
            Map<String, List<String>> groupedKeys = Formatter.format(messageProperties, config);
            writeMessageProperties(groupedKeys, messageProperties, config);
        } catch (IOException e) {
            config.getLog().error("Something went wrong while finding files", e);
        }
    }
}

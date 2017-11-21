package lu.greenhalos.linter.messageproperties.reader;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.Language;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.nio.file.Files.list;
import static java.util.stream.Collectors.toList;


public class Reader {

    private static final Pattern COMPILE_FILENAME = Pattern.compile("messages(_([a-z][a-z]))?\\.properties");
    private static final Pattern COMPILE_LINE = Pattern.compile("([^=]*)=(.*)");
    private final MessageProperties messageProperties;
    private final Config config;

    public Reader(Config config) throws IOException {

        this.config = config;

        List<String> messagesProperties;
        try (Stream<Path> files = list(Paths.get(config.getDirectory()))) {
            messagesProperties = files
                    .filter(p -> p.getFileName().toString().startsWith(config.getPrefix()) && p.getFileName().toString().endsWith(config.getSuffix()))
                    .peek(p -> config.getLog().info("Found file by name: " + p.getFileName()))
                    .map(Path::toString)
                    .collect(toList());
        }
        config.getLog().info("Found " + messagesProperties.size() + " files");
        this.messageProperties = read(messagesProperties);
    }

    private MessageProperties read(List<String> filenames) {

        MessageProperties properties = new MessageProperties();

        filenames.forEach(f -> readFile(f, properties));

        return properties;
    }


    private void readFile(String fileName, MessageProperties properties) {

        Language language = extractLanguage(fileName);

        try(Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.filter(l -> !l.isEmpty()).forEach(l -> extractLine(l, language, properties));
        } catch (IOException e) {
            config.getLog().error("Something went wrong while reading file " + fileName, e);
        }
    }


    private void extractLine(String line, Language language, MessageProperties properties) {

        Matcher matcher = COMPILE_LINE.matcher(line);

        if (matcher.find()) {
            properties.addProperty(matcher.group(1), language, matcher.group(2));
        } else {
            config.getLog().info("couldn't read line: " + line);
        }
    }


    private static Language extractLanguage(String fileName) {

        Matcher matcher = COMPILE_FILENAME.matcher(fileName);

        if (matcher.find()) {
            String languageCode = matcher.group(2);

            return new Language(languageCode);
        }

        throw new IllegalArgumentException();
    }


    public MessageProperties getMessageProperties() {

        return messageProperties;
    }
}

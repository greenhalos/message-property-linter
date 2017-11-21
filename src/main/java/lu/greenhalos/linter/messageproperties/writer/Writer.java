package lu.greenhalos.linter.messageproperties.writer;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.Language;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;

import org.apache.maven.plugin.logging.Log;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


public class Writer {

    private static final String SLASH = "/";

    private Writer() {
    }

    public static void writeMessageProperties(Map<String, List<String>> groupedKeys,
        MessageProperties messageProperties, Config config) {

        messageProperties.getLanguages().forEach(l -> {
            String filePath = config.getDirectory() + SLASH + config.getPrefix() + l.getFileExtension()
                + config.getSuffix();
            List<String> content = groupedKeys.keySet()
                .stream()
                .map(kp -> {
                        List<String> result = groupedKeys.get(kp)
                            .stream()
                            .map(k -> {
                                    Map<Language, String> languageStringMap = messageProperties.getProperties().get(k);

                                    if (languageStringMap.containsKey(l)) {
                                        String value = languageStringMap.get(l);

                                        return k + "=" + value;
                                    } else {
                                        return null;
                                    }
                                })
                            .filter(Objects::nonNull)
                            .collect(toList());

                        if (!result.isEmpty()) {
                            result.add("");
                        }

                        return result;
                    })
                .flatMap(Collection::stream)
                .collect(toList());
            content.remove(content.size() - 1);

            Log log = config.getLog();
            log.info("writing " + content.size() + " lines to file: " + filePath);

            if (config.isDryRun()) {
                content.forEach(c -> log.info("\t" + c));
            } else {
                try {
                    Files.write(Paths.get(filePath), content);
                } catch (IOException e) {
                    log.error("something went wrong while writing to file: " + filePath, e);
                }
            }
        });
    }
}

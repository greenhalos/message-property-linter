package lu.greenhalos.linter.messageproperties.linter;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.Language;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


public class Linter {

    private Linter() {
    }

    public static List<MissingMessagePropertyKey> validate(MessageProperties messageProperties) {

        List<Language> languages = messageProperties.getLanguages();

        return messageProperties.getProperties()
            .keySet()
            .stream()
            .filter(k -> !messageProperties.getProperties().get(k).keySet().containsAll(languages))
            .map(k -> new MissingMessagePropertyKey(k, languages, messageProperties.getProperties().get(k).keySet()))
            .collect(toList());
    }

    public static class MissingMessagePropertyKey {

        private final String propertyKey;
        private final List<Language> requestedLanguages;
        private final Set<Language> foundLanguages;
        private final Set<Language> missingLanguages;

        MissingMessagePropertyKey(String propertyKey, List<Language> requestedLanguages,
            Set<Language> foundLanguages) {

            this.propertyKey = propertyKey;
            this.requestedLanguages = requestedLanguages;
            this.foundLanguages = foundLanguages;
            this.missingLanguages = new HashSet<>(requestedLanguages);
            missingLanguages.removeAll(foundLanguages);
        }

        public void logError(Config config) {

            config.getLog()
                .error("Missing language for key \"" + propertyKey + "\": expected languages: " + requestedLanguages
                    + " but found " + foundLanguages);
            missingLanguages.forEach(l ->
                    config.getLog()
                    .error(
                        "\t" + config.getDirectory() + "/" + config.getPrefix() + l.getFileExtension()
                        + config.getSuffix()));
        }
    }
}

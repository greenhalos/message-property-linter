package lu.greenhalos.linter.messageproperties.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageProperties {

    private Map<String, Map<Language, String>> properties = new HashMap<>();
    private List<Language> languages = new ArrayList<>();

    public void addProperty(String key, Language language, String message) {

        addLanguage(language);

        Map<Language, String> messages = this.properties.getOrDefault(key, new HashMap<>());

        if (messages.containsKey(language)) {
            String oldMessage = messages.get(language);
            throw new LanguageAlreadySetException(language, key, message, oldMessage);
        } else {
            messages.put(language, message);
            properties.put(key, messages);
        }
    }


    private void addLanguage(Language language) {

        if (!languages.contains(language)) {
            languages.add(language);
        }
    }


    public Map<String, Map<Language, String>> getProperties() {

        return properties;
    }


    @Override
    public String toString() {

        return "MessageProperties{"
            + "properties=" + properties
            + ", languages=" + languages + '}';
    }


    public List<Language> getLanguages() {

        return languages;
    }


    public List<String> getKeys() {

        return new ArrayList<>(this.properties.keySet());
    }
}

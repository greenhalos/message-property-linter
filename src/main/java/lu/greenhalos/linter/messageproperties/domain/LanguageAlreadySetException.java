package lu.greenhalos.linter.messageproperties.domain;

public class LanguageAlreadySetException extends RuntimeException {

    private final String oldMessage;
    private final Language language;
    private final String key;
    private final String message;

    LanguageAlreadySetException(Language language, String key, String message, String oldMessage) {

        this.language = language;
        this.key = key;
        this.message = message;
        this.oldMessage = oldMessage;
    }

    public String getOldMessage() {

        return oldMessage;
    }


    public Language getLanguage() {

        return language;
    }


    public String getKey() {

        return key;
    }


    @Override
    public String getMessage() {

        return message;
    }
}

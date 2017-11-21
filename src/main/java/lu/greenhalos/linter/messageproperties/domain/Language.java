package lu.greenhalos.linter.messageproperties.domain;

public class Language {

    private final String code;

    public Language(String code) {

        this.code = code == null ? "DEFAULT" : code;
    }

    @Override
    public String toString() {

        return code;
    }


    public String getFileExtension() {

        return code.equals("DEFAULT") ? "" : "_" + code.toLowerCase();
    }
}

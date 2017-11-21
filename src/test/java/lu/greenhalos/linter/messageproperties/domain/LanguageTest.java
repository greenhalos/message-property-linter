package lu.greenhalos.linter.messageproperties.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageTest {

    private Language sut;

    @Test
    public void getFileExtensionDefault() {
        sut = new Language(null);
        assertThat(sut.getFileExtension()).isEqualTo("");
    }

    @Test
    public void getFileExtensionLanguage() {
        sut = new Language("language");
        assertThat(sut.getFileExtension()).isEqualTo("_language");

        sut = new Language("lAnGuAgE");
        assertThat(sut.getFileExtension()).isEqualTo("_language");
    }

}
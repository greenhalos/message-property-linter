package lu.greenhalos.linter.messageproperties.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MessagePropertiesTest {

    private MessageProperties sut;
    private Language lang1;

    @Before
    public void setUp() {

        sut = new MessageProperties();

        lang1 = new Language("lang1");

    }

    @Test
    public void init() {
        assertThat(sut.getProperties()).isEmpty();
        assertThat(sut.getLanguages()).isEmpty();
        assertThat(sut.getKeys()).isEmpty();
        assertThat(sut.toString()).isEqualTo("MessageProperties{properties={}, languages=[]}");
    }

    @Test
    public void addProperty() {
        sut.addProperty("key1", lang1, "message1");
        Map<String, Map<Language, String>> properties = singletonMap("key1", singletonMap(lang1,"message1"));
        assertThat(sut.getProperties()).isEqualTo(properties);
        assertThat(sut.getLanguages()).hasSize(1);
        assertThat(sut.getLanguages().get(0)).isEqualTo(lang1);
        assertThat(sut.getKeys()).hasSize(1);
        assertThat(sut.getKeys().get(0)).isEqualTo("key1");

    }

    @Test(expected = LanguageAlreadySetException.class)
    public void addPropertyDoubleLanguage() {
        sut.addProperty("key1", lang1, "message1");
        sut.addProperty("key1", lang1, "message2");
    }

}
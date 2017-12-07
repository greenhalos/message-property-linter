package lu.greenhalos.linter.messageproperties.linter;

import lu.greenhalos.linter.messageproperties.domain.Language;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.Arrays.asList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class LinterTest {

    private MessageProperties messageproperties;
    private Language defaultLanguage;
    private Language en;
    private Language ru;
    private List<Language> allLanguages;

    @Before
    public void setUp() {

        defaultLanguage = new Language(null);
        en = new Language("en");
        ru = new Language("ru");
        allLanguages = asList(defaultLanguage, en, ru);

        messageproperties = new MessageProperties();
        messageproperties.addProperty("key1", defaultLanguage, "key1MsgDefault");
        messageproperties.addProperty("key1", en, "key1MsgEn");
        messageproperties.addProperty("key1", ru, "key1MsgRu");

        messageproperties.addProperty("key2", defaultLanguage, "key2MsgDefault");
        messageproperties.addProperty("key2", ru, "key2MsgRu");

        messageproperties.addProperty("key3", defaultLanguage, "key3MsgDefault");
    }


    @Test
    public void validate() {

        List<Linter.MissingMessagePropertyKey> result = Linter.validate(messageproperties);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPropertyKey()).isEqualTo("key2");
        assertThat(result.get(0).getFoundLanguages()).hasSize(2).contains(defaultLanguage, ru);
        assertThat(result.get(0).getMissingLanguages()).hasSize(1).contains(en);
        assertThat(result.get(0).getRequestedLanguages()).hasSize(3).isEqualTo(allLanguages);

        assertThat(result.get(1).getPropertyKey()).isEqualTo("key3");
        assertThat(result.get(1).getFoundLanguages()).hasSize(1).contains(defaultLanguage);
        assertThat(result.get(1).getMissingLanguages()).hasSize(2).contains(en, ru);
        assertThat(result.get(1).getRequestedLanguages()).hasSize(3).isEqualTo(allLanguages);
    }
}

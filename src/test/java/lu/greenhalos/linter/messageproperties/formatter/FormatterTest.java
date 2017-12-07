package lu.greenhalos.linter.messageproperties.formatter;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.Language;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;

import org.apache.maven.plugin.logging.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class FormatterTest {

    private MessageProperties messageProperties;
    private Config config;

    @Before
    public void setUp() {

        Log loggerMock = mock(Log.class);
        config = new Config.Builder(null, null, null, loggerMock).build();

        Language en = new Language("EN");
        messageProperties = new MessageProperties();
        messageProperties.addProperty("lala.key2.field1", en, "message1");
        messageProperties.addProperty("lala.key1.field2", en, "message2");
        messageProperties.addProperty("lala.key1.field1", en, "message1");
        messageProperties.addProperty("lala.key1.field4", en, "message4");
        messageProperties.addProperty("lala.key1.field7", en, "message7");
        messageProperties.addProperty("lala.key1.field5", en, "message5");
        messageProperties.addProperty("foo.keyfoo.fieldfoo", en, "messagefoo");
        messageProperties.addProperty("lala.key1.field9", en, "message9");
        messageProperties.addProperty("lala.key1.field10", en, "message10");
        messageProperties.addProperty("lala.key1.field3", en, "message3");
        messageProperties.addProperty("lala.key1.field6", en, "message6");
        messageProperties.addProperty("lala.key1.field8", en, "message8");
    }


    @Test
    public void format() {

        Map<String, List<String>> result = Formatter.format(messageProperties, config);

        Map<String, List<String>> resultingMap = new HashMap<>();
        resultingMap.put("foo", singletonList("foo.keyfoo.fieldfoo"));
        resultingMap.put("lala.key1",
            asList("lala.key1.field1", "lala.key1.field10", "lala.key1.field2", "lala.key1.field3", "lala.key1.field4",
                "lala.key1.field5", "lala.key1.field6", "lala.key1.field7", "lala.key1.field8", "lala.key1.field9"));
        resultingMap.put("lala.key2", singletonList("lala.key2.field1"));

        assertThat(result).hasSize(3).isEqualTo(resultingMap);
    }
}

package lu.greenhalos.linter.messageproperties.formatter;

import lu.greenhalos.linter.messageproperties.domain.Config;
import lu.greenhalos.linter.messageproperties.domain.MessageProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lu.greenhalos.linter.messageproperties.writer.Writer.writeMessageProperties;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;


public class Formatter {

    private static final Pattern COMPILE_LINE = Pattern.compile("([^.]*)(\\..*)*");
    private static final int MAX_GROUP_SIZE = 8;

    private Formatter() {
    }

    public static void format(MessageProperties messageProperties, Config config) {

        Map<String, List<String>> groupedKeys = group(messageProperties.getKeys());
        config.getLog().info("found " + groupedKeys.size() + " groups");
        writeMessageProperties(groupedKeys, messageProperties, config);
    }


    private static Map<String, List<String>> group(List<String> keys) {

        Map<String, List<String>> groups = createGroups(keys, "");
        List<String> keysToIgnore = new ArrayList<>();

        List<String> oversizeKeys = getOversizeKeys(groups, keysToIgnore);

        while (!oversizeKeys.isEmpty()) {
            oversizeKeys.forEach(k -> {
                Map<String, List<String>> smallerGroups = createGroups(groups.get(k), k + ".");

                if (smallerGroups.values().stream().map(List::size).max(Integer::compareTo).orElse(0) > 5) {
                    groups.remove(k);
                    groups.putAll(smallerGroups);
                } else {
                    keysToIgnore.add(k);
                }
            });
            oversizeKeys = getOversizeKeys(groups, keysToIgnore);
        }

        return groups;
    }


    private static List<String> getOversizeKeys(Map<String, List<String>> groups, List<String> keysToIgnore) {

        return groups.keySet().stream().filter(k -> !keysToIgnore.contains(k)).filter(k ->
                    groups.get(k).size() > MAX_GROUP_SIZE).collect(toList());
    }


    /**
     * @param  prefixKey  should end with a dot
     */
    private static Map<String, List<String>> createGroups(List<String> keys, String prefixKey) {

        Map<String, List<String>> result = keys.stream()
                .filter(k -> k.length() < prefixKey.length())
                .collect(groupingBy(k -> k, TreeMap::new, mapping(k -> k,
                            collectingAndThen(toList(), (l -> l.stream().sorted().collect(toList()))))));
        TreeMap<String, List<String>> restMap = keys.stream()
                .filter(k -> k.length() >= prefixKey.length())
                .map(k -> k.substring(prefixKey.length()))
                .map(COMPILE_LINE::matcher)
                .filter(Matcher::find)
                .collect(groupingBy(m -> prefixKey + m.group(1), TreeMap::new, mapping(m -> prefixKey + m.group(0),
                            collectingAndThen(toList(), (l -> l.stream().sorted().collect(toList()))))));

        result.putAll(restMap);

        return result;
    }
}

# Message Property Linter [![Build Status](https://travis-ci.org/greenhalos/message-property-linter.svg?branch=message-properties-linter-maven-plugin-1.0)](https://travis-ci.org/greenhalos/message-property-linter)

Simple Maven Plugin for linting and formatting message properties in your project.

Currently only works with Java 8.

## How to use

add the repository to your `pom.xml`
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

then add the plugin to your `pom.xml`
```xml
 <plugin>
    <groupId>com.github.greenhalos</groupId>
    <artifactId>message-property-linter</artifactId>
    <version>${linter.version}</version>
    <configuration>

        <!--Optional: directory, where the files are located. Default: src/main/resources-->
        <directory>src/main/resources</directory>

        <!--Optional: suffix with which the files are ending. Default: .properties-->
        <suffix>.properties</suffix>

        <!--Optional: prefix with which every file starts. Default: messages-->
        <prefix>messages</prefix>

        <!--Optional: When true, the formatter will not change the files. No impact on the linter. Default: false-->
        <dryRun>true</dryRun>

    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>formatter</goal>
            </goals>
            <phase>compile</phase>
        </execution>
    </executions>
</plugin>
```

## How to release

1. Change version in `pom.xml`
1. push to master
1. create tag with `v#.#`
1. push tag
1. Change version in `pom.xml` to match next `SNAPSHOT`-version

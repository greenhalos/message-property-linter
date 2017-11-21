package lu.greenhalos.linter.messageproperties.domain;

import org.apache.maven.plugin.logging.Log;


public class Config {

    private final String directory;
    private final String prefix;
    private final String suffix;
    private final boolean dryRun;
    private final Log log;

    private Config(String directory, String prefix, String suffix, boolean dryRun, Log log) {

        this.directory = directory;
        this.prefix = prefix;
        this.suffix = suffix;
        this.dryRun = dryRun;
        this.log = log;
    }

    public String getDirectory() {

        return directory;
    }


    public String getPrefix() {

        return prefix;
    }


    public String getSuffix() {

        return suffix;
    }


    public boolean isDryRun() {

        return dryRun;
    }


    public Log getLog() {

        return log;
    }


    @Override
    public String toString() {

        return "Config{"
            + "directory='" + directory + '\''
            + ", prefix='" + prefix + '\''
            + ", suffix='" + suffix + '\''
            + ", dryRun=" + dryRun
            + ", log=" + log + '}';
    }

    public static class Builder {

        private String directory = "src/main/resources";
        private String prefix = "messages";
        private String suffix = "properties";
        private Log log;
        private boolean dryRun = false;

        public Builder(String directory, String prefix, String suffix, Log log) {

            this.directory = directory;
            this.prefix = prefix;
            this.suffix = suffix;
            this.log = log;
        }

        public Builder setDryRun(boolean dryRun) {

            this.dryRun = dryRun;

            return this;
        }


        public Config build() {

            Config config = new Config(directory, prefix, suffix, dryRun, log);
            log.info("Running with config: " + config);

            return config;
        }
    }
}

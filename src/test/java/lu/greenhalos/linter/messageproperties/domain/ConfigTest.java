package lu.greenhalos.linter.messageproperties.domain;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ConfigTest {

    private Config sut;

    @Mock
    private Log logMock;

    @Test
    public void setDryRun() {
        sut = new Config.Builder("dir", "prefix", "suffix", logMock).setDryRun(true).build();
        assertConfig(true);
    }

    @Test
    public void build() {
        sut = new Config.Builder("dir", "prefix", "suffix", logMock).build();
        assertConfig(false);
    }

    private void assertConfig(boolean isDryRun) {
        assertThat(sut.getDirectory()).isEqualTo("dir");
        assertThat(sut.getPrefix()).isEqualTo("prefix");
        assertThat(sut.getSuffix()).isEqualTo("suffix");
        assertThat(sut.getLog()).isEqualTo(logMock);
        assertThat(sut.isDryRun()).isEqualTo(isDryRun);
        verify(logMock).info("Running with config: Config{directory='dir', prefix='prefix', suffix='suffix', dryRun=" + (isDryRun ? "true" : "false") + ", log=logMock}");
    }

}
package styx.studio.dex.config;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import styx.studio.dex.shell.ProgressBar;
import styx.studio.dex.shell.ProgressCounter;
import styx.studio.dex.shell.PromptColor;
import styx.studio.dex.shell.Shell;
import styx.studio.dex.shell.input.InputReader;

@Configuration
public class SpringShellConfig {

  @Bean
  public Shell shellHelper(@Lazy Terminal terminal) {
    return new Shell(terminal);
  }

  @Bean
  public JaroWinklerDistance jaroWinklerDistance() {
    return new JaroWinklerDistance();
  }

  @Bean
  public InputReader inputReader(
      @Lazy Terminal terminal,
      @Lazy Parser parser,
      JLineShellAutoConfiguration.CompleterAdapter completer,
      @Lazy History history,
      Shell shell) {
    LineReaderBuilder lineReaderBuilder =
        LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(completer)
            .history(history)
            .highlighter(
                (LineReader reader, String buffer) ->
                    new AttributedString(
                        buffer,
                        AttributedStyle.BOLD.foreground(
                            PromptColor.WHITE.toJlineAttributedStyle())))
            .parser(parser);

    LineReader lineReader = lineReaderBuilder.build();
    lineReader.unsetOpt(LineReader.Option.INSERT_TAB);
    return new InputReader(lineReader, shell);
  }

  @Bean
  public ProgressBar progressBar(Shell shell) {
    return new ProgressBar(shell);
  }

  @Bean
  public ProgressCounter progressCounter(@Lazy Terminal terminal) {
    return new ProgressCounter(terminal);
  }
}

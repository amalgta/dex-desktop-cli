package styx.studio.dex.shell;

import java.util.Arrays;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;

public class Shell {

  @Value("${shell.out.info}")
  private String infoColor;

  @Value("${shell.out.success}")
  private String successColor;

  @Value("${shell.out.warning}")
  private String warningColor;

  @Value("${shell.out.error}")
  private String errorColor;

  private Terminal terminal;

  @Value("${app.logging.time-stamp-pattern}")
  private String timestampPattern;

  public Shell(Terminal terminal) {
    this.terminal = terminal;
  }

  /**
   * Construct colored message in the given color.
   *
   * @param message message to return
   * @param color color to print
   * @return colored message
   */
  private String getColored(String message, PromptColor color) {
    return (new AttributedStringBuilder())
        .append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle()))
        .toAnsi();
  }

  public String getColored(String message, Level level) {
    PromptColor color;
    switch (level) {
      case ERROR:
        color = PromptColor.valueOf(errorColor);
        break;
      case SUCCESS:
        color = PromptColor.valueOf(successColor);
        break;
      case WARNING:
        color = PromptColor.valueOf(warningColor);
        break;
      case INFO:
      default:
        color = PromptColor.valueOf(infoColor);
        break;
    }
    return getColored(message, color);
  }

  public String getInfoMessage(String message) {
    return getColored(message, Level.INFO);
  }

  public String getSuccessMessage(String message) {
    return getColored(message, Level.SUCCESS);
  }

  public String getWarningMessage(String message) {
    return getColored(message, Level.WARNING);
  }

  public String getErrorMessage(String message) {
    return getColored(message, Level.ERROR);
  }

  /**
   * Print message to the console in the default color.
   *
   * @param message message to print
   */
  public void print(String message) {
    print(message, null);
  }

  // --- Print methods -------------------------------------------------------

  /**
   * Print message to the console in the success color.
   *
   * @param message message to print
   */
  public void success(String message) {
    print(message, Level.SUCCESS);
  }

  /**
   * Print message to the console in the info color.
   *
   * @param message message to print
   */
  public void info(String message, String... params) {
    print(MessageFormatter.arrayFormat(message, params).getMessage(), Level.INFO);
  }

  public static String format(String message, Object... params) {
    return MessageFormatter.arrayFormat(message, params).getMessage();
  }

  /**
   * Print message to the console in the warning color.
   *
   * @param message message to print
   */
  public void printWarning(String message) {
    print(message, Level.WARNING);
  }

  /**
   * Print message to the console in the error color.
   *
   * @param message message to print
   */
  public void error(String message) {
    print(message, Level.ERROR);
  }

  public void error(String message, Throwable throwable) {
    print(message, Level.ERROR);
    print(throwable.toString(), Level.ERROR);
    Arrays.stream(throwable.getStackTrace()).forEach(t -> print(t.toString(), Level.ERROR));
  }

  /**
   * Generic Print to the console method.
   *
   * @param message message to print
   * @param level (optional) prompt level
   */
  private void print(String message, Level level) {
    String toPrint = message;
    if (level != null) {
      toPrint = getColored(toPrint, level);
    }
    terminal.writer().println(toPrint);
    terminal.flush();
  }

  public Terminal getTerminal() {
    return terminal;
  }

  // --- set / get methods ---------------------------------------------------

  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }

  enum Level {
    INFO,
    ERROR,
    SUCCESS,
    WARNING
  }
}

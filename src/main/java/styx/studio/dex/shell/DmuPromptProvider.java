package styx.studio.dex.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class DmuPromptProvider implements PromptProvider {
  @Value("${shell.prompt}")
  private String shellPrompt;

  @Override
  public AttributedString getPrompt() {
    return new AttributedString(
        shellPrompt, AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
  }
}

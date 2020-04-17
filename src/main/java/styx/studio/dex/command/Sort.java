package styx.studio.dex.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@Slf4j
public class Sort {
  public String sort(
      @ShellOption(defaultValue = "") String downloadDirectory,
      @ShellOption({"-R", "--remove-file"}) boolean isRemoveFiles) {
    return "";
  }
}

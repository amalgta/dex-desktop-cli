package styx.studio.dex.shell.input.option;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BinaryOption {
  YES,
  NO;

  public static List<String> toList() {
    return Arrays.stream(values()).map(Enum::name).collect(Collectors.toList());
  }
}

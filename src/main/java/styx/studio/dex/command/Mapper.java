package styx.studio.dex.command;

import java.util.Map;

public interface Mapper<T> {
  Map<String, String> get();
}

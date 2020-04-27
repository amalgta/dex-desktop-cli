package styx.studio.dex.command;

import java.util.HashMap;
import java.util.Map;
import styx.studio.dex.domain.metadata.MovieFileMetadata;

public class MetadataMapper implements Mapper<MovieFileMetadata> {

  private HashMap<String, String> map = new HashMap<>();

  MetadataMapper(MovieFileMetadata metadata) {
    map.put("original_language", metadata.getOriginalLanguage());
    map.put("title", metadata.getTitle());
    map.put("year", metadata.getYear().toString());
    map.put("extension", metadata.getFileExtension());
  }

  @Override
  public Map<String, String> get() {
    return map;
  }
}

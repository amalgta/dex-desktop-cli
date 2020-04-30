package styx.studio.dex.domain.metadata;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import styx.studio.dex.shell.Shell;

@Getter
@Setter
@Data
@Builder
public class MovieFileMetadata {
  protected Integer id;
  protected String title;
  protected Integer year;
  protected String originalLanguage;
  protected String fileExtension;

  @Override
  public String toString() {
    return Shell.format(
        "id:{},title:{},year:{},originalLanguage:{},fileExtension:{}",
        getId(),
        getTitle(),
        getYear(),
        getOriginalLanguage(),
        getFileExtension());
  }
}

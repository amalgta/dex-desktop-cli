package styx.studio.dex.domain.metadata;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
}

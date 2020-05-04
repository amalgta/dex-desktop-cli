package styx.studio.dex.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import styx.studio.dex.domain.error.ErrorCode;
import styx.studio.dex.domain.error.ErrorReport;
import styx.studio.dex.domain.metadata.MovieFileMetadata;

@Component
public class MovieFinder {
  public interface MovieCallBack {
    void onMovieFile(File file, MovieFileMetadata metadata);
  }

  @Autowired private ErrorReport errorReport;

  @Value("${dex.file-types}")
  private List<String> supportedFileFormats;

  @Value("${dex.language}")
  private String language;

  @Autowired private MovieFileMetadataGenerator metadataGenerator;

  public ErrorReport find(Path path, MovieCallBack movieCallBack) throws IOException {
    errorReport.clear();
    Files.walk(path)
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .forEach(
            file -> {
              if (FilenameUtils.indexOfExtension(file.getName()) == -1) {
                errorReport.report(ErrorCode.NoExtension, file);
                return;
              }
              if (!FilenameUtils.isExtension(file.getName(), supportedFileFormats)) {
                errorReport.report(ErrorCode.InvalidExtension, file);
                return;
              }
              MovieFileMetadata metadata = metadataGenerator.generateMetadata(file, language);
              if (metadata != null) {
                movieCallBack.onMovieFile(file, metadata);
              } else {
                errorReport.report(ErrorCode.MetadataGenerationFailed, file);
              }
            });
    return errorReport;
  }
}

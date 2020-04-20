package styx.studio.dex.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import styx.studio.dex.domain.error.Exte;
import styx.studio.dex.domain.metadata.MovieFileMetadata;
import styx.studio.dex.service.MovieFileMetadataGenerator;
import styx.studio.dex.shell.Shell;

@ShellComponent
@Slf4j
public class Sort {

  @Value("${dex.file-types}")
  private List<String> sortableFileTypes;

  @Autowired private Shell shell;

  @Autowired private MovieFileMetadataGenerator movieFileMetadataGenerator;
  private HashMap<Exte, List<File>> errorList = new HashMap<>();

  @ShellMethod("Sorts movie files.")
  public String sort(
      @ShellOption(defaultValue = "C:\\Users\\ndh00316\\Desktop\\Writing\\") String directory,
      @ShellOption(defaultValue = "C:\\Users\\ndh00316\\Desktop\\Sorted\\")
          String targetDirectory) {
    try {
      try {
        Files.walk(Paths.get(directory))
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .forEach(
                file -> {
                  if (FilenameUtils.indexOfExtension(file.getName()) == -1) {
                    errorList.computeIfAbsent(Exte.InvalidExtension, k -> new ArrayList<>());
                    errorList.get(Exte.InvalidExtension).add(file);
                    return;
                  }
                  if (!FilenameUtils.isExtension(file.getName(), sortableFileTypes)) {
                    return;
                  }
                  MovieFileMetadata metadata = movieFileMetadataGenerator.generateMetadata(file);
                  write(metadata, targetDirectory, file);
                });
      } catch (IOException e) {
        throw e;
      }
    } catch (Exception e) {
      shell.error("potto", e);
    }
    return "";
  }

  private void write(MovieFileMetadata metadata, String targetDirectory, File file) {
    try {
      FileUtils.moveFile(
          file,
          new File(
              targetDirectory
                  + File.separatorChar
                  + metadata.getOriginalLanguage()
                  + File.separatorChar
                  + metadata.getTitle()
                  + " ["
                  + metadata.getYear()
                  + "]"
                  + metadata.getFileExtension()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

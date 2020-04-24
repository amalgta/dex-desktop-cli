package styx.studio.dex.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import styx.studio.dex.common.utils.StringTemplate;
import styx.studio.dex.domain.metadata.MovieFileMetadata;
import styx.studio.dex.service.MovieFinder;
import styx.studio.dex.shell.Shell;

@ShellComponent
@Slf4j
public class Movie {

  @Autowired private MovieFinder movieFinder;
  @Autowired private Shell shell;

  @ShellMethod("Sorts movie files.")
  public String sort(
      @ShellOption(defaultValue = "C:\\Users\\ndh00316\\Desktop\\Writing\\") String directory,
      @ShellOption(defaultValue = "C:\\Users\\ndh00316\\Desktop\\Sorted\\")
          String targetDirectory) {
    try {
      Path directoryPath = Paths.get(directory);
      if (!directoryPath.toFile().exists()) {
        throw new IllegalArgumentException(directory + " doesn't exist.");
      }
      movieFinder.find(directoryPath, (file, metadata) -> write(metadata, targetDirectory, file));
    } catch (Exception e) {
      shell.error("Sort has failed", e);
    }
    return "";
  }

  private void write(MovieFileMetadata metadata, String targetDirectory, File sourceFile) {
    try {
      StringTemplate directory = new StringTemplate("$original_language$");
      directory.setAttribute("original_language", metadata.getOriginalLanguage());
      StringTemplate fileName = new StringTemplate("$title$ [$year$].$extension$");
      fileName.setAttribute("title", metadata.getTitle());
      fileName.setAttribute("year", metadata.getYear().toString());
      fileName.setAttribute("extension", metadata.getFileExtension());

      String directoryPath =
          targetDirectory
              + File.separatorChar
              + "sorted"
              + File.separatorChar
              + directory.toString();
      String filePath = fileName.toString().replaceAll("[:\\\\/*?|<>]", "-");

      File targetFile = new File(directoryPath, filePath);
      if (targetFile.exists()) {
        StringTemplate duplicateDirectory =
            new StringTemplate("$original_language$\\$title$ [$year$]");
        duplicateDirectory.setAttribute("original_language", metadata.getOriginalLanguage());
        duplicateDirectory.setAttribute("title", metadata.getTitle());
        duplicateDirectory.setAttribute("year", String.valueOf(metadata.getYear()));
        String duplicateDirectoryPath =
            targetDirectory
                + File.separatorChar
                + "duplicates"
                + File.separatorChar
                + duplicateDirectory.toString();
        File duplicateDirectoryFolder = new File(duplicateDirectoryPath);

        StringTemplate duplicateDirectoryFileName1 =
            new StringTemplate("$title$ [$year$] [$count$].$extension$");
        duplicateDirectoryFileName1.setAttribute("title", metadata.getTitle());
        duplicateDirectoryFileName1.setAttribute("year", metadata.getYear().toString());
        duplicateDirectoryFileName1.setAttribute("extension", metadata.getFileExtension());

        StringTemplate duplicateDirectoryFileName2 =
            new StringTemplate("$title$ [$year$] [$count$].$extension$");
        duplicateDirectoryFileName2.setAttribute("title", metadata.getTitle());
        duplicateDirectoryFileName2.setAttribute("year", metadata.getYear().toString());
        duplicateDirectoryFileName2.setAttribute("extension", metadata.getFileExtension());

        if (duplicateDirectoryFolder.exists()
            && Objects.requireNonNull(duplicateDirectoryFolder.list()).length > 0) {
          String existingFile =
              Arrays.stream(Objects.requireNonNull(duplicateDirectoryFolder.listFiles()))
                  .map(File::getName)
                  .reduce((first, second) -> second)
                  .orElse(null);
          String temp =
              "(?<title>[a-zA-Z \\)\\(]+) *\\[(?<year>[0-9]{4})\\] \\[(?<count>[0-9]+)\\].[a-z0-9]{2,4}";
          int count = Integer.parseInt(Pattern.compile(temp).matcher(existingFile).group("count"));
          duplicateDirectoryFileName1.setAttribute("count", String.valueOf(count++));
          duplicateDirectoryFileName2.setAttribute("count", String.valueOf(count));
        } else {
          duplicateDirectoryFileName1.setAttribute("count", String.valueOf(1));
          duplicateDirectoryFileName2.setAttribute("count", String.valueOf(2));
        }
        FileUtils.moveFile(
            sourceFile, new File(duplicateDirectoryFolder, duplicateDirectoryFileName1.toString()));
        FileUtils.moveFile(
            targetFile, new File(duplicateDirectoryFolder, duplicateDirectoryFileName2.toString()));
      } else {
        FileUtils.moveFile(sourceFile, targetFile);
      }
    } catch (IOException e) {
      shell.error("Write has failed", e);
    }
  }
}

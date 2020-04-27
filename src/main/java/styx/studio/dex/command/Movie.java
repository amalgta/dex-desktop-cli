package styx.studio.dex.command;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${dex.output.file-structure}")
  private String outputFileStructure;

  @Value("${dex.output.duplicate-file-structure}")
  private String duplicateFileStructure;

  @Value("${dex.output.folder-structure}")
  private String outputFolderStructure;

  @Value("${dex.output.duplicate-folder-structure}")
  private String duplicateFolderStructure;

  @Value("${dex.output.duplicate-file-structure.regexp}")
  private String outputRegex;

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
      movieFinder.find(directoryPath, (file, metadata) -> write(file, metadata, targetDirectory));
    } catch (Exception e) {
      shell.error("Sort has failed", e);
    }
    return "Sort has completed";
  }

  private void write(File sourceFile, MovieFileMetadata metadata, String targetDirectory) {
    try {
      Mapper metadataMapper = new MetadataMapper(metadata);
      String outputFileName =
          new StringTemplate.StringTemplateBuilder()
              .template(outputFileStructure)
              .addMapper(metadataMapper)
              .removeAll("[:\\\\/*?|<>]")
              .build()
              .get();
      String outputRelativeDirectory =
          new StringTemplate.StringTemplateBuilder()
              .template(outputFolderStructure)
              .addMapper(metadataMapper)
              .build()
              .get();
      String duplicateRelativeDirectory =
          new StringTemplate.StringTemplateBuilder()
              .template(duplicateFolderStructure)
              .addMapper(metadataMapper)
              .build()
              .get();
      String duplicateCompleteDirectory =
          targetDirectory
              + File.separatorChar
              + "duplicates"
              + File.separatorChar
              + duplicateRelativeDirectory;
      String outputCompleteDirectory =
          targetDirectory
              + File.separatorChar
              + "sorted"
              + File.separatorChar
              + outputRelativeDirectory;

      File duplicateFolder = new File(duplicateCompleteDirectory);
      if (duplicateFolder.exists()) {
        String existingFileName =
            Arrays.stream(Objects.requireNonNull(duplicateFolder.listFiles()))
                .map(File::getName)
                .reduce((first, second) -> second)
                .orElse(null);
        Matcher matcher = Pattern.compile(outputRegex).matcher(existingFileName);
        if (matcher.find()) {
          AtomicInteger finalCount = new AtomicInteger(Integer.parseInt(matcher.group("count")));
          String duplicateFileNameSourceFile =
              new StringTemplate.StringTemplateBuilder()
                  .template(duplicateFileStructure)
                  .addMapper(metadataMapper)
                  .addMapper(
                      () ->
                          Collections.singletonMap(
                              "count", String.valueOf(finalCount.incrementAndGet())))
                  .removeAll("[:\\\\/*?|<>]")
                  .build()
                  .get();
          FileUtils.moveFile(sourceFile, new File(duplicateFolder, duplicateFileNameSourceFile));
        }
      } else {
        File outputFile = new File(outputCompleteDirectory, outputFileName);
        if (outputFile.exists()) {
          AtomicInteger count = new AtomicInteger();
          String duplicateFileNameSourceFile =
              new StringTemplate.StringTemplateBuilder()
                  .template(duplicateFileStructure)
                  .addMapper(metadataMapper)
                  .addMapper(
                      () ->
                          Collections.singletonMap(
                              "count", String.valueOf(count.incrementAndGet())))
                  .removeAll("[:\\\\/*?|<>]")
                  .build()
                  .get();
          String duplicateFileNameTargetFile =
              new StringTemplate.StringTemplateBuilder()
                  .template(duplicateFileStructure)
                  .addMapper(metadataMapper)
                  .addMapper(
                      () ->
                          Collections.singletonMap(
                              "count", String.valueOf(count.incrementAndGet())))
                  .removeAll("[:\\\\/*?|<>]")
                  .build()
                  .get();
          FileUtils.moveFile(sourceFile, new File(duplicateFolder, duplicateFileNameSourceFile));
          FileUtils.moveFile(outputFile, new File(duplicateFolder, duplicateFileNameTargetFile));
        } else {
          FileUtils.moveFile(sourceFile, outputFile);
        }
      }
    } catch (IOException e) {
      shell.error("Write has failed", e);
    }
  }
}

package styx.studio.dex.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import styx.studio.dex.command.Mapper;
import styx.studio.dex.command.MetadataMapper;
import styx.studio.dex.common.utils.StringTemplate;
import styx.studio.dex.domain.metadata.MovieFileMetadata;
import styx.studio.dex.shell.Shell;

@Service
public class MovieWriter {
  @Autowired private Shell shell;

  @Value("${dex.output.file-structure}")
  private String outputFileStructure;

  @Value("${dex.output.duplicate-folder-structure}")
  private String duplicateFolderStructure;

  @Value("${dex.output.folder-structure}")
  private String outputFolderStructure;

  @Value("${dex.output.duplicate-file-structure}")
  private String duplicateFileStructure;

  @Value("${dex.output.duplicate-file-structure.regexp}")
  private String outputRegex;

  public void write(File sourceFile, MovieFileMetadata metadata, String targetDirectory) {
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
          File targetFile = new File(duplicateFolder, duplicateFileNameSourceFile);
          shell.info(
              "{} ({}) -> {}", sourceFile.getName(), metadata.getTitle(), targetFile.getName());
          FileUtils.moveFileToDirectory(sourceFile, targetFile, true);
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
          FileUtils.moveFileToDirectory(
              sourceFile, new File(duplicateFolder, duplicateFileNameSourceFile), true);
          FileUtils.moveFileToDirectory(
              outputFile, new File(duplicateFolder, duplicateFileNameTargetFile), true);
        } else {
          FileUtils.moveFileToDirectory(sourceFile, outputFile, true);
        }
      }
    } catch (IOException e) {
      shell.error("Write has failed", e);
    }
  }
}

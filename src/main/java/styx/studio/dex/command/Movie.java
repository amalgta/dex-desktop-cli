package styx.studio.dex.command;

import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import styx.studio.dex.service.MovieFinder;
import styx.studio.dex.service.MovieWriter;
import styx.studio.dex.shell.Shell;

@ShellComponent
@Slf4j
public class Movie {

  @Autowired private MovieFinder movieFinder;
  @Autowired private Shell shell;
  @Autowired private MovieWriter writer;

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
      shell.info("Starting sort");
      shell.info("directory : {}", directory);
      shell.info("targetDirectory : {}", targetDirectory);
      movieFinder.find(
          directoryPath,
          (file, metadata) -> {
            shell.info("{} : {}", file.getName(), metadata.toString());
            writer.write(file, metadata, targetDirectory);
          });
    } catch (Exception e) {
      shell.error("Sort has failed", e);
    }
    return "Sort has completed";
  }
}

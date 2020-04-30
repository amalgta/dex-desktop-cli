package styx.studio.dex;

import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import retrofit2.Response;
import styx.studio.dex.shell.Shell;

@SpringBootApplication
@EnableAutoConfiguration
@Slf4j
public class DesktopCliApplication {
  @Autowired Shell shell;
  @Autowired SearchService searchService;

  public static void main(String[] args) {
    SpringApplication.run(DesktopCliApplication.class, args);
  }

  @PostConstruct
  private void afterInitialization() throws IOException {
    Response<MovieResultsPage> response =
        searchService.movie("Kayamkulam Kochunni", null, "", "", false, 2018, 2018).execute();
    if (response.isSuccessful()) {
      shell.success("API Tested for " + response.body().results.get(0).title);
    } else {
      shell.error("API Failed");
    }
  }
}

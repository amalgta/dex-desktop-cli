package styx.studio.dex;

import com.uwetrottmann.tmdb2.services.SearchService;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import styx.studio.dex.shell.Shell;

@SpringBootApplication
@EnableAutoConfiguration
@Slf4j
public class DesktopCliApplication {
  @Autowired private Shell shell;
  @Autowired private SearchService searchService;

  @Value("${api.key}")
  private String tMDBApiKey;

  public static void main(String[] args) {
    SpringApplication.run(DesktopCliApplication.class, args);
  }

  @PostConstruct
  private void afterInitialization() {
    shell.info("Initializing Dex CLI");
    if (StringUtils.isEmpty(tMDBApiKey)) {
      shell.error("Api key not found.");
    } else {
      String maskedKey = tMDBApiKey.replaceAll("\\b(\\w{2})\\w+(\\w{3})", "$1*******$2");
      shell.info("Api key : {}", maskedKey);
    }
    try {
      if (searchService
          .movie("Kayamkulam Kochunni", null, "", "", false, 2018, 2018)
          .execute()
          .isSuccessful()) {
        shell.success("Connection success");
      } else {
        throw new Exception("Api failure response");
      }
    } catch (Exception e) {
      shell.error("Api test has failed", e);
    }
  }
}

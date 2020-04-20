package styx.studio.dex.config;

import com.uwetrottmann.tmdb2.Tmdb;
import com.uwetrottmann.tmdb2.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TMDBConfig {

  @Bean
  public Tmdb tmdb() {
    return new Tmdb("Ayyada");
  }

  @Bean
  public SearchService searchService(@Autowired Tmdb tmdb) {
    return tmdb.searchService();
  }
}

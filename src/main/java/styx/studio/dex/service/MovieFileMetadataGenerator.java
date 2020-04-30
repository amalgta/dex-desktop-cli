package styx.studio.dex.service;

import com.uwetrottmann.tmdb2.entities.BaseMovie;
import com.uwetrottmann.tmdb2.entities.MovieResultsPage;
import com.uwetrottmann.tmdb2.services.SearchService;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import styx.studio.dex.domain.metadata.MovieFileMetadata;
import styx.studio.dex.domain.repository.PatternRepository;

@Component
public class MovieFileMetadataGenerator {

  @Autowired private PatternRepository patternRepository;
  @Autowired private SearchService searchService;
  @Autowired private JaroWinklerDistance jaroWinklerDistance;

  public MovieFileMetadata generateMetadata(File file) {
    MovieFileMetadata metadata = null;
    Iterable<styx.studio.dex.domain.pattern.Pattern> pattern = patternRepository.findAll();
    for (styx.studio.dex.domain.pattern.Pattern format : pattern) {
      Matcher matcher = Pattern.compile(format.getPattern()).matcher(file.getName());
      if (matcher.find()) {
        metadata =
            MovieFileMetadata.builder()
                .title(matcher.group("title"))
                .year(Integer.parseInt(matcher.group("year")))
                .build();
        // TODO what if multiple patterns match ?
        break;
      }
    }
    if (metadata == null) {
      return null;
    }
    metadata.setFileExtension(FilenameUtils.getExtension(file.getName()));

    try {
      Response<MovieResultsPage> response =
          searchService
              .movie(
                  metadata.getTitle(), null, "", "", false, metadata.getYear(), metadata.getYear())
              .execute();
      if (response.isSuccessful()) {
        MovieFileMetadata finalMetadata = metadata;
        List<BaseMovie> movie = Objects.requireNonNull(response.body()).results;
        Optional<BaseMovie> m;
        if (movie.isEmpty()) return null;
        if (movie.stream().anyMatch(t -> t.title.equals(finalMetadata.getTitle().trim()))) {
          m =
              movie.stream()
                  .filter(t -> t.title.equals(finalMetadata.getTitle().trim()))
                  .findFirst();
        } else {
          m =
              movie.stream()
                  .min(
                      (movie1, movie2) -> {
                        double movie1Distance =
                            jaroWinklerDistance.apply(
                                movie1.title, finalMetadata.getTitle().trim());
                        double movie2Distance =
                            jaroWinklerDistance.apply(
                                movie2.title, finalMetadata.getTitle().trim());
                        if (movie1Distance < movie2Distance) return -1;
                        if (movie1Distance > movie1Distance) return 1;
                        return 0;
                      });
        }
        BaseMovie bm = m.get();
        metadata.setTitle(bm.title);
        metadata.setOriginalLanguage(getOriginalLanguage(bm.original_language));
        metadata.setId(bm.id);
      } else {
      }
    } catch (Exception e) {
    }
    return metadata;
  }

  private String getOriginalLanguage(String originalLanguage) {
    HashMap<String, String> translation = new HashMap<>();
    translation.put("en", "English");
    translation.put("ml", "Malayalam");
    translation.put("ta", "Tamil");
    translation.put("te", "Telugu");
    translation.put("hi", "Hindi");
    translation.put("kn", "Kannada");
    if (translation.containsKey(originalLanguage)) {
      return translation.get(originalLanguage);
    }
    return null;
  }
}

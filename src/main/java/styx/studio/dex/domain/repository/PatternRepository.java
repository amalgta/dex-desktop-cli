package styx.studio.dex.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import styx.studio.dex.domain.pattern.Pattern;

@Transactional
public interface PatternRepository extends CrudRepository<Pattern, Long> {}

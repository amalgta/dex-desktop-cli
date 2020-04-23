package styx.studio.dex.domain.pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
@Entity
@Setter
@Getter
@Table(name = "patterns")
public class Pattern {
  @Id
  @Column(name = "ID", unique = true, updatable = false, nullable = false)
  @GeneratedValue
  private long id;

  @Column(name = "type")
  private String type;

  @Column(name = "name")
  private String name;

  @Column(name = "pattern")
  private String pattern;

  @Column(name = "sample")
  private String sample;
}

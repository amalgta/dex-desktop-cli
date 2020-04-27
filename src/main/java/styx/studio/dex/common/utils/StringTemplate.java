package styx.studio.dex.common.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;
import styx.studio.dex.command.Mapper;

public class StringTemplate {
  private String string = "";

  public StringTemplate(String string) {
    this.string = string;
  }

  public void setAttribute(String key, String value) {
    this.string = this.string.replace(String.format("$%s$", key), value);
  }

  @Override
  public String toString() {
    return this.string;
  }

  public String get() {
    return this.toString();
  }

  public void removeAll(String regex, String replacement) {
    this.string = this.string.replaceAll(regex, replacement);
  }

  public void removeAll(String regex) {
    removeAll(regex, "-");
  }

  public void map(Mapper metadataMapper) {
    metadataMapper.get().forEach((key, value) -> setAttribute(key.toString(), value.toString()));
  }

  public static class StringTemplateBuilder {
    private String template;
    private List<Mapper> mappers;
    private String removeAll;

    public StringTemplateBuilder() {}

    public StringTemplateBuilder template(String template) {
      this.template = template;
      return this;
    }

    public StringTemplateBuilder addMapper(Mapper mapper) {
      if (this.mappers == null) {
        this.mappers = new ArrayList<>();
      }
      this.mappers.add(mapper);
      return this;
    }

    public StringTemplate build() {
      StringTemplate stringTemplate = new StringTemplate(template);
      mappers.forEach(stringTemplate::map);
      if (!StringUtils.isEmpty(removeAll)) {
        stringTemplate.removeAll(removeAll);
      }
      return stringTemplate;
    }

    public StringTemplateBuilder removeAll(String s) {
      this.removeAll = s;
      return this;
    }
  }
}

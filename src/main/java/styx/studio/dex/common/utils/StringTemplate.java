package styx.studio.dex.common.utils;

public class StringTemplate {
  private String string = "";

  public StringTemplate(String string) {
    this.string = string;
  }

  public void setAttribute(String key, String value) {
    this.string = this.string.replace("$" + key + "$", value);
  }

  @Override
  public String toString() {
    return this.string;
  }
}

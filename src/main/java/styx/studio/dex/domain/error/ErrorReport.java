package styx.studio.dex.domain.error;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ErrorReport {
  private HashMap<ErrorCode, List<File>> errorList = new HashMap<>();

  public void clear() {
    errorList.clear();
  }

  public boolean report(ErrorCode errorCode, File file) {
    return errorList.computeIfAbsent(errorCode, k -> new ArrayList<>()).add(file);
  }

  public HashMap<ErrorCode, List<File>> getErrorList() {
    return errorList;
  }
}

package styx.studio.dex.common.executor;

import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import styx.studio.dex.common.constant.Messages;
import styx.studio.dex.common.executor.Result.State;

/** A helper class to execute actions exclusively. */
public class ExclusiveExecutor {

  /**
   * A method that accepts a path and executable, creates a exclusive lock file at the path and
   * executes the operation, if lock file is not being used by any other process, and returns the
   * result and error if any.
   *
   * @param lockFilePath The path of file to be used as lock
   * @param executable The action to be executed
   * @return The result of the run.
   */
  public static Result run(Path lockFilePath, Executable executable) {
    Result result = new Result();
    try {
      try (FileChannel fileChannel =
              FileChannel.open(lockFilePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
          FileLock lock = fileChannel.tryLock()) {
        if (lock == null) {
          result.setState(State.Failed);
          result.setError(new RuntimeException(Messages.ERROR_MULTIPLE_INSTANCE_RUNNING));
        } else {
          if (executable.execute()) {
            result.setState(State.Success);
          } else {
            result.setState(State.Failed);
          }
        }
      }
    } catch (Exception e) {
      result.setState(State.Failed);
      result.setError(new RuntimeException(e));
    }
    return result;
  }

  public interface Executable {
    boolean execute();
  }
}

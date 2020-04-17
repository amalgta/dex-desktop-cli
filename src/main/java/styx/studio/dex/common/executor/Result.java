package styx.studio.dex.common.executor;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Result {
  State state;
  Throwable error;

  Result() {
    state = State.Waiting;
    error = null;
  }

  public State getState() {
    return state;
  }
  public enum State {
    Waiting,
    Success,
    Failed
  }
}

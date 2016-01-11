package client;

import java.util.Map;

public interface Strategy {
  
  public Move determineMove(Board board);

}

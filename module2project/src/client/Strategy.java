package client;

import java.util.Map;

public interface Strategy {
  
  public Map<int[], String> determineMove(Board board);

}

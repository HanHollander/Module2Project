package client.model;

import java.util.List;
import java.util.Map;

public interface Strategy {
  
  public Move determineMove(Board board, List<Tile> hand, ComputerPlayer player);

}

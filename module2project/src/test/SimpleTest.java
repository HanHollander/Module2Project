package test;

import java.util.ArrayList;
import java.util.List;

import client.Board;
import client.Game;
import client.HumanPlayer;
import client.Player;
import client.Tile;
import exceptions.HandIsFullException;

public class SimpleTest {
  
  public static void main(String[] args) {
    List<Player> players = new ArrayList<Player>();
    players.add(new HumanPlayer("p1", 1));
    Game game = new Game(players, players.get(0));
    Board board = new Board();
    System.out.println(board.toString());
    try {
      players.get(0).addToHand(new Tile("R", "o"));
      players.get(0).addToHand(new Tile("B", "o"));
    } catch (HandIsFullException e) {
      System.out.println(e);
    }
    game.playerTurn();
  }

}

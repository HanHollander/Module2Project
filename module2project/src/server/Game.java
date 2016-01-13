package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import client.Move.Type;
import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public class Game {
    
  //Fields\\
  
  public static final List<String> COLOURS = Arrays.asList("R", "O", "B", "Y", "G", "P");
  public static final List<String> SHAPES = Arrays.asList("o", "d", "s", "c", "x", "*");
  
  private Board board;
  private HashMap<Integer, Player> playerList;
  private int currentPlayer;
  private ArrayList<Tile> pool;
  private Server server;
  
  //Constructor\\
  
  public Game(Server server) {
    this.server = server;
    board = new Board();
    playerList = new HashMap<Integer, Player>();
    currentPlayer = 0;
    pool = new ArrayList<Tile>();
    
    for (int i = 0; i < 3; i++) {
      for (String color : COLOURS) {
        for (String shape : SHAPES) {
          pool.add(new Tile(color, shape));
        }
      }
    }
  }
  
  public Boolean checkTurn(List<Move> turn) {
    Boolean result = true;
    for (int i = 0; i < turn.size(); i++) {
      result = result && board.checkMove(turn.get(i));
    }
    return result;
  }
  
  public void applyTurn(Player player, List<Move> turn) {
    for (int i = 0; i < turn.size(); i++) {
      Move move = turn.get(i);
      board.putTile(move);
      try {
        player.removeFromHand(move.getTile());
      } catch (TileNotInHandException e) {
        System.out.println("Tried to remove tile [" + move.getTile().toString() + "] from the hand of (" + player.toString() + ")"
            + " but the tile was not in his/her hand.");
      }
    }
    player.addToScore(board.getScoreCurrentTurn());
    board.endTurn();
  }
  
  public int getCurrentPlayer() {
    return currentPlayer;
  }
  
  public Server getServer() {
    return server;
  }
  
  public Tile swapTileWithPool(Tile tile) {
    addTileToPool(tile);
    return drawRandomTileFromPool();
  }
  
  public void addTileToPool(Tile tile) {
    pool.add(tile);
  }
  
  /**
   * Takes a random tile from the pool and removes it from the pool.
   * @return A random tile from the pool.
   */
  public Tile drawRandomTileFromPool() {
    int randomIndex = (int)Math.round(Math.random() * (pool.size() - 1));
    Tile result = pool.get(randomIndex);
    pool.remove(randomIndex);
    return result;
  }
  
  public void addPlayer(int playerNr, String name) {
    
  }
  
}

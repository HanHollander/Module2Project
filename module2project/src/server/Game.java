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
import java.util.Set;

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
  
  public Boolean checkTurn(List<Move> turn, Player player) {
    Boolean result = true;
    for (int i = 0; i < turn.size(); i++) {
      Move move = turn.get(i);
      result = result && board.checkMove(move);
      result = result && player.getHand().contains(move.getTile());
    }
    return result;
  }
  
  public int getCurrentPlayer() {
    return currentPlayer;
  }
  
  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
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
  
  public synchronized void addPlayer(int playerNr, String name) {
    synchronized (playerList) {
      playerList.put(playerNr, new Player(name, playerNr));
    }
  }
  
  public Player getPlayer(int playerNr) {
    return playerList.get(playerNr);
  }
  
  public void removePlayer(int playerNr) {
    playerList.remove(playerNr);
  }
  
  public Board getBoard() {
    return board;
  }
  
  public Set<Integer> getPlayerNrs() {
    return playerList.keySet();
  }
  
  public void dealTiles() {
    Set<Integer> playerNrs = getPlayerNrs();
    for (int playerNr : playerNrs) {
      Player player = getPlayer(playerNr);
      for (int i = 0; i < 6; i++) {
        try {
          player.addToHand(drawRandomTileFromPool());
        } catch (HandIsFullException e) {
          System.out.println("HandIsFullException occured while dealing the tiles");
        }
      }
    }
  }
  
  public int getPlayerNrWithTheBestHand() {
    Set<Integer> playerNrs = getPlayerNrs();
    int bestPossibleHandPointsYet = -1;
    int playerNrWithBestPossibleHandPointsYet = 0;
    int bestRowLengthYet = -1;
    for (int playerNr : playerNrs) {
      List<Tile> hand = getPlayer(playerNr).getHand();
      for (Tile tile : hand) {
        bestRowLengthYet = -1;
        List<Tile> rowWithShapeTheSame = new ArrayList<Tile>();
        List<String> rowWithShapeTheSameColors = new ArrayList<String>();
        List<Tile> rowWithColorTheSame = new ArrayList<Tile>();
        List<String> rowWithColorTheSameShapes = new ArrayList<String>();
        rowWithShapeTheSame.add(tile);
        rowWithShapeTheSameColors.add(tile.getColor());
        rowWithColorTheSame.add(tile);
        rowWithColorTheSameShapes.add(tile.getShape());
        for (Tile tile2 : hand) {
          if (tile.getShape().equals(tile2.getShape()) 
              && !rowWithShapeTheSameColors.contains(tile2.getColor())) {
            rowWithShapeTheSame.add(tile2);
            rowWithShapeTheSameColors.add(tile2.getColor());
          }
          
          if (tile.getColor().equals(tile2.getColor()) 
              && !rowWithColorTheSameShapes.contains(tile2.getShape())) {
            rowWithColorTheSame.add(tile2);
            rowWithColorTheSameShapes.add(tile2.getColor());
          }
        }
        if (rowWithShapeTheSame.size() > bestRowLengthYet) {
          bestRowLengthYet = rowWithShapeTheSame.size();
        }
        
        if (rowWithColorTheSame.size() > bestRowLengthYet) {
          bestRowLengthYet = rowWithColorTheSame.size();
        }
      }
      if (bestRowLengthYet > bestPossibleHandPointsYet) {
        bestPossibleHandPointsYet = bestRowLengthYet;
        playerNrWithBestPossibleHandPointsYet = playerNr;
      }
    }
    return playerNrWithBestPossibleHandPointsYet;
  }
  
}

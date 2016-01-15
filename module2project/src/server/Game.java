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
    board = new Board(this);
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
  
  public void applySwapTurn(List<Tile> turn, Player player) {
    List<Tile> newTiles = new ArrayList<Tile>();
    for (Tile tile : turn) {
      try {
        player.removeFromHand(tile);
      } catch (TileNotInHandException e) {
        System.out.println(e);
      }
      try {
        player.addToHand(swapTileWithPool(tile));
      } catch (HandIsFullException e) {
        System.out.println(e);
      }
    }
    server.giveTiles(player.getPlayerNumber(), newTiles);
    server.broadcast("TURN " + player.getPlayerNumber() + " empty");
  }
  
  public void applyMoveTurn(Player player, List<Move> turn, boolean isFirstTurn) {
    //Put tiles on the board
    for (int i = 0; i < turn.size(); i++) {
      Move move = turn.get(i);
      board.putTile(move);
      try {
        player.removeFromHand(move.getTile());
      } catch (TileNotInHandException e) {
        System.out.println("Tried to remove tile [" + move.getTile().toString() + "]"
            + " from the hand of (" + player.toString() + ")"
            + " but the tile was not in his/her hand.");
      }
    }
    //Get tiles from the pool
    List<Tile> tilesBack = new ArrayList<Tile>();
    for (int i = 0; i < turn.size() && pool.size() > 0; i++) {
      Tile tile = drawRandomTileFromPool();
      try {
        player.addToHand(tile);
      } catch (HandIsFullException e) {
        System.out.println(e);
      }
      tilesBack.add(tile);
    }
    
    if (!isFirstTurn) {
      server.giveTiles(player.getPlayerNumber(), tilesBack);
    }
    server.sendTurn(player.getPlayerNumber(), turn);
    player.addToScore(board.getScoreCurrentTurn());
    board.endTurn();
  }
  
  public boolean checkSwapTurn(List<Tile> turn , Player player) {
    boolean result = true;
    List<Tile> hand = player.getHand();
    for (Tile tile : turn) {
      boolean containsTile = false;
      for (Tile tileInHand : hand) {
        containsTile = tile.equals(tileInHand);
        if (containsTile) {
          hand.remove(tileInHand);
          break;
        }
      }
      result = result && containsTile;
    }
    return result;
  }
  
  public boolean checkTurn(List<Move> turn, Player player) {
    Boolean result = true;
    Board testBoard = board.deepCopy();
    List<Tile> hand = player.getHand();
    
    for (int i = 0; i < turn.size(); i++) {
      
      Move move = turn.get(i);
      Tile tile = move.getTile();
      for (Tile tileInHand : hand) {
        boolean containsTile = tile.equals(tileInHand);
        
        result = result || containsTile;
        if (containsTile) {
          hand.remove(tileInHand);
          break;
        }
      }
      if (!result) {
        break;
      }
      System.out.println("Testing move: " + move.toString());
      result = result && testBoard.checkMove(move);
      System.out.println("Result = " + result);
      if (result) {
        testBoard.putTile(move);
      } else {
        break;
      }
      
      
    }
    return result;
  }
  
  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }
  
  public Tile swapTileWithPool(Tile tile) {
    addTileToPool(tile);
    return drawRandomTileFromPool();
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

  public void addTileToPool(Tile tile) {
    pool.add(tile);
  }
  
  public synchronized void addPlayer(int playerNr, String name) {
    synchronized (playerList) {
      playerList.put(playerNr, new Player(name, playerNr));
    }
  }
  
  public void removePlayer(int playerNr) {
    playerList.remove(playerNr);
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
    System.out.println("Player-" + getWinningPlayerNr() + " started");
    for (int playerNr : playerNrs) {
      server.getThread(playerNr).sendMessage("NEW" + getPlayer(playerNr).handToString());
    }
  }
  
  public int getWinningPlayerNr() {
    Set<Integer> playerNrs = playerList.keySet();
    int highestPointsYet = -1;
    int playerNrWithhighestPointsYet = 0;
    for (int playerNr : playerNrs) {
      int score = getPlayer(playerNr).getScore();
      if (score > highestPointsYet) {
        highestPointsYet = score;
        playerNrWithhighestPointsYet = playerNr;
      }
    }
    return playerNrWithhighestPointsYet;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public Player getPlayer(int playerNr) {
    return playerList.get(playerNr);
  }

  public Server getServer() {
    return server;
  }

  public Board getBoard() {
    return board;
  }
  
  public Set<Integer> getPlayerNrs() {
    return playerList.keySet();
  }
  
  public int getPoolSize() {
    return pool.size();
  }
  
  public int getPlayerNrWithTheBestHand() {
    Set<Integer> playerNrs = getPlayerNrs();
    int bestPossibleHandPointsYet = -1;
    int playerNrWithBestPossibleHandPointsYet = 0;
    List<Tile> overAllBestRow = new ArrayList<Tile>();
    int bestRowLengthYet = -1;
    for (int playerNr : playerNrs) {
      List<Tile> hand = getPlayer(playerNr).getHand();
      bestRowLengthYet = -1;
      List<Tile> bestRow = new ArrayList<Tile>();
      //System.out.println("Calculating best hand for player-" + playerNr);
      for (Tile tile : hand) {
        
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
        //System.out.println("rowWithShapeTheSame: " + rowWithShapeTheSame);
        if (rowWithShapeTheSame.size() > bestRowLengthYet) {
          //System.out.println("Bigger than previous");
          bestRowLengthYet = rowWithShapeTheSame.size();
          bestRow.addAll(rowWithShapeTheSame);
        }
        //System.out.println("rowWithColorTheSame: " + rowWithColorTheSame);
        if (rowWithColorTheSame.size() > bestRowLengthYet) {
          //System.out.println("Bigger than previous");
          bestRowLengthYet = rowWithColorTheSame.size();
          bestRow.addAll(rowWithColorTheSame);
        }
      }
      if (bestRowLengthYet > bestPossibleHandPointsYet) {
        bestPossibleHandPointsYet = bestRowLengthYet;
        playerNrWithBestPossibleHandPointsYet = playerNr;
        overAllBestRow.addAll(bestRow);
        //System.out.println("Player-" + playerNr + "got the best hand yet");
      }
    }
    List<Move> turn = new ArrayList<Move>();
    int row = 91;
    int column = 91;
    for (Tile tile : overAllBestRow) {
      turn.add(new Move(tile, row, column));
      column++;
    }
    applyMoveTurn(getPlayer(playerNrWithBestPossibleHandPointsYet), turn, true);
    return playerNrWithBestPossibleHandPointsYet;
  }
  
}

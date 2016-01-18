package server;

import exceptions.HandIsFullException;
import exceptions.TileNotInHandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


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
  /**
   * The Constructor of a game.
   * @param server The server through which the game can communicate with the players.
   */
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
  
  /**
   * Applies the given turn to the pool and sends it to all the players.
   * @param player The player that made the turn.
   * @param turn The turn that needs to be applied.
   */
  public void applySwapTurn(List<Tile> turn, Player player) {
    List<Tile> newTiles = new ArrayList<Tile>();
    for (Tile tile : turn) {
      try {
        player.removeFromHand(tile);
      } catch (TileNotInHandException e) {
        System.out.println(e);
      }
      try {
        Tile newTile = swapTileWithPool(tile);
        player.addToHand(newTile);
        newTiles.add(newTile);
      } catch (HandIsFullException e) {
        System.out.println(e);
      }
    }
    server.giveTiles(player.getPlayerNumber(), newTiles);
    server.broadcast("TURN " + player.getPlayerNumber() + " empty");
  }
  
  /**
   * Applies the given turn to the board and sends it to all the players.
   * @param player The player that made the turn.
   * @param turn The turn that needs to be applied.
   * @param isFirstTurn True or False whether this is the first turn of the game or not.
   */
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
  
  /**
   * Checks if the tiles that the given player wants to swap
   * are all in the current hand of the player.
   * @param turn The tiles that needs to be checked.
   * @param player The player who made/suggested the turn.
   * @return True of False whether the turn is valid or not.
   */
  public boolean checkSwapTurn(List<Tile> turn , Player player) {
    boolean result = true;
    if (getPoolSize() >= turn.size()) {
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
    } else {
      result = false;
    }
    return result;
  }
  
  /**
   * Checks if the turn is valid according to the Qwirkle rules.
   * @param turn The turn that needs to be checked.
   * @param player The player who made/suggested the turn.
   * @return True of False whether the turn is valid or not.
   */
  public boolean checkMoveTurn(List<Move> turn, Player player) {
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
  
  /**
   * Swaps a tile with a random tile from the pool.
   * @param tile The tile that goes into the pool.
   * @return The tile that comes out of the pool.
   */
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
  
  /**
   * Gives every player 6 tiles and sends those to the players personally.
   */
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
    System.out.println("Player-" + getPlayerNrWithTheBestHand() + " started");
    for (int playerNr : playerNrs) {
      server.getThread(playerNr).sendMessage("NEW" + getPlayer(playerNr).handToString());
    }
  }

  public synchronized void addPlayer(int playerNr, String name) {
    synchronized (playerList) {
      playerList.put(playerNr, new Player(name, playerNr));
    }
  }
  
  public void removePlayer(int playerNr) {
    synchronized (playerList) {
      playerList.remove(playerNr);
    }
  }
  
  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }
  
  /**
   * Searches for the player with the highest score.
   * @return The player with the highest score.
   */
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
  
  /**
   * Calculates which player has the best move in their hand
   * and applies that move to the board.
   * @return the player who had the best move.
   */
  public int getPlayerNrWithTheBestHand() {
    Set<Integer> playerNrs = getPlayerNrs();
    int playerNrWithBestPossibleHandPointsYet = 0;
    List<Tile> overAllBestRow = new ArrayList<Tile>();
    for (int playerNr : playerNrs) {
      List<Tile> hand = getPlayer(playerNr).getHand();
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
        if (rowWithShapeTheSame.size() > bestRow.size()) {
          //System.out.println("Bigger than previous");
          bestRow = new ArrayList<Tile>();
          bestRow.addAll(rowWithShapeTheSame);
        }
        //System.out.println("rowWithColorTheSame: " + rowWithColorTheSame);
        if (rowWithColorTheSame.size() > bestRow.size()) {
          //System.out.println("Bigger than previous");
          bestRow = new ArrayList<Tile>();
          bestRow.addAll(rowWithColorTheSame);
        }
      }
      if (bestRow.size() > overAllBestRow.size()) {
        playerNrWithBestPossibleHandPointsYet = playerNr;
        overAllBestRow = new ArrayList<Tile>();
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
    setCurrentPlayer(playerNrWithBestPossibleHandPointsYet);
    return playerNrWithBestPossibleHandPointsYet;
  }
  
  /**
   * Checks if the game is over. The game is over when the
   * pool is empty and someone's hand is empty.
   * @return True of False whether the game is over or not.
   */
  public boolean isGameOver() {
    boolean result = getPoolSize() == 0;
    if (result) {
      result = false;
      Set<Integer> playerNrs = playerList.keySet();
      for (int playerNr : playerNrs) {
        if (playerList.get(playerNr).getHand().size() == 0) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
  
}

package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import client.Move.Type;
import exceptions.HandIsFullException;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

/**
 * Human player class.
 * @author Han
 */
public class HumanPlayer extends Player {
  
  public static final String MOVEUSAGE = "Usage: [MOVE <tile> <row> <column>]";
  public static final String SWAPUSAGE = "Usage: [SWAP <tile>]";
  
  public static final List<String> COLOURS = Arrays.asList("R", "O", "B", "Y", "G", "P");
  public static final List<String> SHAPES = Arrays.asList("o", "d", "s", "c", "x", "*");

  /**
   * Constructor for a human.
   * @param name name of human
   * @param playerNumber nr of human
   */
  public HumanPlayer(String name, int playerNumber) {
    super(name, playerNumber);
  }
  
  /**
   * determine human move.
   * @param board board where move can take place
   * @return a valid move
   */
  public Move determineMove(Board board) throws InvalidMoveException {
    boolean validMove = false;
    Move move = null;
    while (!validMove) {
      Type listType;
      System.out.println("Moves: " + getMoves() + " " + getMoves().size());
      if (getMoves().size() == 0) {
        listType = Type.ANY;
      } else {
        listType = getMoves().get(0).getType();
      }
      System.out.println("Listtype: " + listType);
      try {
        move = getMove();
        if (move.getType().equals(Type.MOVE) 
            && (listType.equals(Type.ANY) || listType.equals(Type.MOVE))) {
          if (board.checkMove(move)) {
            try {
              removeFromHand(move.getTile());
            } catch (TileNotInHandException e) {
              System.out.println(e);
            }
            validMove = true;
            getMoves().add(move);
          } else {
            throw new InvalidMoveException();
          }
        } else if (move.getType().equals(Type.SWAP) 
            && (listType.equals(Type.ANY) || listType.equals(Type.SWAP))) {
          try {
            removeFromHand(move.getTile());
          } catch (TileNotInHandException e) {
            System.out.println(e);
          }
          validMove = true;
          getMoves().add(move);
        } else  if (move.getType().equals(Type.END)) {
          validMove = true;
        } else {
          throw new InvalidMoveException();
        }
      } catch (InvalidCommandException e) {
        System.out.println(e);
      }
    }
    return move;
  }
  
  /**
   * Get input from console.
   * @return returns the command, only if move is valid
   * @throws InvalidCommandException no valid command tho
   */
  public Move getMove() throws InvalidCommandException {
    Move move = null;
    String[] command = null;
    String input = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    boolean validInput = false;
    while (!validInput) {
      try {
        System.out.println("What is your action?");
        input = reader.readLine();
      } catch (IOException e) {
        System.out.println("Could not read line.");
      }
      //MOVE command
      if (input.startsWith("MOVE")) {
        validInput = validMoveCommand(input);
        if (validInput) {
          command = input.split(" ");
          String colour = Character.toString(command[1].charAt(0));
          String shape = Character.toString(command[1].charAt(1));
          int row = Integer.parseInt(command[2]);
          int column = Integer.parseInt(command[3]);
          move = new Move(new Tile(colour, shape), row, column);
        }
      //SWAP command
      } else if (input.startsWith("SWAP")) {
        validInput = validSwapCommand(input);
        if (validInput) {
          command = input.split(" ");
          String colour = Character.toString(command[1].charAt(0));
          String shape = Character.toString(command[1].charAt(1));
          move = new Move(new Tile(colour, shape));
        }
      //END command
      } else if (input.startsWith("END")) {
        validInput = validEndCommand(input);
        if (validInput) {
          move = new Move(Type.END);
        }
      } else { //Neither move nor swap
        throw new InvalidCommandException(MOVEUSAGE + " " + SWAPUSAGE);
      }
    }
    return move;
  }
  
  /**
   * Check if move command is valid.
   * @param input input command
   * @return boolean if valid
   * @throws InvalidCommandException not valid
   */
  public boolean validMoveCommand(String input) throws InvalidCommandException {
    boolean validInput = false;
    String[] command = input.split(" "); 
    if (command.length == 4) { //Command length must be four
      String colour = Character.toString(command[1].charAt(0));
      String shape = Character.toString(command[1].charAt(1));
      try { //Row and column must be integers
        int row = Integer.parseInt(command[2]);
        int column = Integer.parseInt(command[3]);
      } catch (NumberFormatException e) { //Throw exception if row or column are invalid
        throw new InvalidCommandException(MOVEUSAGE);
      }
      if (COLOURS.contains(colour) && SHAPES.contains(shape)) { //Colour and shape must exist
        validInput = true;
      } else { //Throw exception if colour or shape is not valid
        throw new InvalidCommandException("Colours: " + COLOURS + ", shapes: " 
            + SHAPES + " " + MOVEUSAGE);
      }
    } else { //Throw exception if command lenght is not four
      throw new InvalidCommandException(MOVEUSAGE);
    }
    return validInput;
  }
  
  /**
   * Check if move command is valid.
   * @param input input command
   * @return boolean if valid
   * @throws InvalidCommandException not valid
   */
  public boolean validSwapCommand(String input) throws InvalidCommandException {
    boolean validInput = false;
    String[] command = input.split(" ");
    if (command.length == 2) { //length must be 2
      String colour = Character.toString(command[1].charAt(0));
      String shape = Character.toString(command[1].charAt(1));
      if (COLOURS.contains(colour) && SHAPES.contains(shape)) { //Colour and shape must exist
        validInput = true;
      } else { //Invalid colour and shape
        throw new InvalidCommandException("Colours: " + COLOURS + ", shapes: " + SHAPES);
      }
    } else { //Invalid command usage
      throw new InvalidCommandException(SWAPUSAGE);
    }
    return validInput;
  }
  
  /**
   * Checks if valid endcommand.
   * @param input input
   * @return if valid
   * @throws InvalidCommandException if not valid
   */
  public boolean validEndCommand(String input) throws InvalidCommandException {
    boolean validInput = false;
    String[] command = input.split(" "); 
    if (command.length == 1) { //Command length must be four
      validInput = true;
    } else {
      throw new InvalidCommandException("Usage: END.");
    }
    return validInput;
  }
  
}

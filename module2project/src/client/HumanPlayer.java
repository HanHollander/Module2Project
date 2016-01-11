package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;

/**
 * Human player class
 * @author Han
 */

public class HumanPlayer extends Player {
  
  public static final String MOVEUSAGE = "Usage: [MOVE <tile> <row> <column>]";
  public static final String SWAPUSAGE = "Usage: [SWAP <tile>]";
  
  public static final List<String> COLOURS = Arrays.asList("R", "O", "B", "Y", "G", "P");
  public static final List<String> SHAPES = Arrays.asList("o", "d", "s", "c", "x", "*");

  public HumanPlayer(String name, int playerNumber) {
    super(name, playerNumber);
  }

  public Map<int[], String> determineMove(Board board) {
    boolean validMove = false;
    Map<int[], String> result = null;
    while (!validMove) {
      Move move = getInput();
      if (board.checkMove(move)) {
        validMove = true;
      } else {
        throw new InvalidMoveException();
      }
    }
    return result;
  }
  
  /**
   * Get input from console
   * @return returns the command, only if move is valid
   * @throws InvalidCommandException
   */
  public Move getInput() throws InvalidCommandException {
    Move move = null;
    String[] command = null;
    String input = "";
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    boolean validInput = false;
    while (!validInput) {
      try {
        input = reader.readLine();
      } catch (IOException e) {
        System.out.println("Could not read line.");
      }
      //MOVE command
      if (input.startsWith("MOVE")) {
        command = input.split(" "); 
        if (command.length == 4) { //Command length must be four
          String colour = Character.toString(command[1].charAt(0));
          String shape = Character.toString(command[1].charAt(1));
          int row;
          int column;
          try { //Row and column must be integers
            row = Integer.parseInt(command[2]);
            column = Integer.parseInt(command[3]);
          } catch (NumberFormatException e) { //Throw exception if row or column are invalid
            throw new InvalidCommandException(MOVEUSAGE);
          }
          if (COLOURS.contains(colour) && SHAPES.contains(shape)) { //Colour and shape must exist
            validInput = true;
            move = new Move(colour, shape, row, column);
          } else { //Throw exception if colour or shape is not valid
            throw new InvalidCommandException("Colours: " + COLOURS + ", shapes: " + SHAPES + " " + MOVEUSAGE);
          }
        } else { //Throw exception if command lenght is not four
          throw new InvalidCommandException(MOVEUSAGE);
        }
      //SWAP command
      } else if (input.startsWith("SWAP")) {
        command = input.split(" ");
        if (command.length == 2) { //length must be 2
          String colour = Character.toString(command[1].charAt(0));
          String shape = Character.toString(command[1].charAt(1));
          if (COLOURS.contains(colour) && SHAPES.contains(shape)) { //Colour and shape must exist
            validInput = true;
            move = new Move(colour, shape);
          } else { //Invalid colour and shape
            throw new InvalidCommandException("Colours: " + COLOURS + ", shapes: " + SHAPES);
          }
        } else { //Invalid command usage
          throw new InvalidCommandException(SWAPUSAGE);
        }
      } else { //Neither move nor swap
        throw new InvalidCommandException(MOVEUSAGE + " " + SWAPUSAGE);
      }
    }
    return move;
  }

}

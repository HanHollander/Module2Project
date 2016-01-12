package exceptions;

public class InvalidMoveException extends Exception {
  
  public InvalidMoveException() {
    super("The move is not valid.");
  }

}

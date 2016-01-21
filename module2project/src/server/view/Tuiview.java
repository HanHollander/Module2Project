package server.view;

import server.controller.Server;
import server.model.Game;

import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class Tuiview implements Observer {

  private Server server;
  private PrintStream output;
  
  /**
   * Creates a new Tuiview for the given server.
   * @param server Server
   */
  public Tuiview(Server server) {
    this.server = server;
    output = System.out;
  }
  
  /**
   * Prints a message to System.out with the number of the game server in front of the message.
   * @param msg Message
   */
  public void print(String msg) {
    output.println("Game-" + server.getServerNr() + ": " + msg);
  }
  
  /**
   * Prints the board situation to System.out.
   */
  public void update(Observable observable, Object arg) {
    if (arg.equals("turn made")) {
      Game game = server.getGame();
      output.println("\n" + "Game " + server.getServerNr() + ": Current game situation");
      output.println(game.getBoard().toString());
      output.println("Scores:");
      Set<Integer> playerNrs = server.getPlayerNrs();
      for (int playerNr : playerNrs) {
        output.println(game.getPlayer(playerNr).getName() + ": " 
            + game.getPlayer(playerNr).getScore());
      }
      output.println("Hands");
      for (int playerNr : playerNrs) {
        output.println(game.getPlayer(playerNr).getName() + ": " 
            + game.getPlayer(playerNr).getHand().toString());
      }
      output.println("Tiles in pool: " + server.getGame().getPoolSize());
    }
  }

}

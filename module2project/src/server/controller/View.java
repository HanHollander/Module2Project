package server.controller;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import server.model.Game;

public class View implements Observer {

  private Server server;
  
  public View(Server server) {
    this.server = server;
  }
  
  public void update(Observable o, Object arg) {
    if (arg.equals("turn made")) {
      Game game = server.getGame();
      System.out.println("Game " + server.getServerNr() + ": Current game situation");
      System.out.println(game.getBoard().toString());
      System.out.println("Scores:");
      Set<Integer> playerNrs = server.getPlayerNrs();
      for (int playerNr : playerNrs) {
        System.out.println(game.getPlayer(playerNr).getName() + ": " 
            + game.getPlayer(playerNr).getScore());
      }
      System.out.println("Hands");
      for (int playerNr : playerNrs) {
        System.out.println(game.getPlayer(playerNr).getName() + ": " 
            + game.getPlayer(playerNr).getHand().toString());
      }
      System.out.println("Tiles in pool: " + game.getPoolSize());
    }
  }

}

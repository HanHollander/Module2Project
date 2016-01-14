package client.test;

import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;

import client.Client;
import client.Game;

public class ClientTest {
  
  private Client c;

  @Before
  public void setUp() throws Exception {
    c = new Client("Game", InetAddress.getByName("localhost"), 7777, 
        new Game("Game", InetAddress.getByName("localhost"), 7777, "h"));
  }

  @Test
  public void test() {
    fail("Not yet implemented");
  }

}

package server.test.controller;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import server.controller.*;

public class ClientHandlerTest {

  private Server s;
  private ClientHandler ch;
  
  @Before
  public void setUp() {
    s = new Server(null, 2, 999999, null, 1);
    ch = new ClientHandler(1, s);
  }
  @Test
  public void test() {
    assertEquals(null, ch.getClientName());
  }

}

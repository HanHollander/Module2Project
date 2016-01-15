package server;

public class Timer extends Thread{
  
  private Server server;
  private Object timerMonitor;
  private long time;
  private boolean stoppedFromTheOutside;
  
  public Timer(int time, Server server) {
    this.server = server;
    this.time = new Long(time);
    this.stoppedFromTheOutside = false;
    timerMonitor = new Object();
  }
  
  public void run() {
    synchronized (timerMonitor) {
      try {
        System.out.println("Timer is waiting for " + time + " ms");
        timerMonitor.wait(time);
      } catch (InterruptedException e) {
        System.out.println("Timer thread got interupted");
      }
      if (!stoppedFromTheOutside) {
        server.timerWakesServer();
      }
    }
  }
  
  public void stopTimer() {
    stoppedFromTheOutside = true;
    synchronized (timerMonitor) {
      timerMonitor.notifyAll();
    }
  }
  
}

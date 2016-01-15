package server;

public class Timer extends Thread{
  
  private Object monitor;
  private Object timerMonitor;
  private int time;
  
  public Timer(Object monitor, Object timerMonitor, int time) {
    this.monitor = monitor;
    this.timerMonitor = timerMonitor;
    this.time = time;
  }
  
  public void run() {
    synchronized (timerMonitor) {
      timerMonitor.wait(time);
    }
  }
  
}

import java.util.concurrent.Semaphore;

public class ConcurrentCounter {
	private int counter;
	private Semaphore upSemaphore = new Semaphore(1);
    private Semaphore downSemaphore = new Semaphore(0);

    public int getCounter() {
		return counter;
	}
    
    public void setCounter(int value) {
    	if (counter != value) {
    		this.counter = value;
    	}
    }
    
    public int incrementCounter() {
        return counter++;
    }

    public int decrementCounter() {
        return counter--;
    }
    
    public void count(int start, int end, String counterName) {
        try {
        	setCounter(start);
        	if (start < end) {
                upSemaphore.acquire(); // Acquire the upSemaphore
                while (counter < end) {
                	System.out.println(counterName + ": " + incrementCounter());
                }
                System.out.println(counterName + ": " + getCounter()); // Print last value
                downSemaphore.release(); // Release the downSemaphore
        	} else {
        		downSemaphore.acquire(); // Acquire the downSemaphore
                while (counter > end) {
                	System.out.println(counterName + ": " + decrementCounter());
                }
                System.out.println(counterName + ": " + getCounter()); // Print last value
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	public static void main(String[] args) {
		ConcurrentCounter counter = new ConcurrentCounter();

        Thread counterUpThread = new Thread(() -> {
            counter.count(counter.getCounter(), 20, "Thread 1 counter"); // Count up from 0 to 20
        });

        Thread counterDownThread = new Thread(() -> {
            counter.count(counter.getCounter(), 0, "Thread 2 counter"); // Count down from 20 to 0
        });

        counterUpThread.start();
        counterDownThread.start();
	}
}

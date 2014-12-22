package de.uni.konstanz.tests;

public class ThreadingTest {
	
	static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format( "%s: %s%n", threadName, 
				message);
	}
	
	private static class MessageLoop implements Runnable {

		@Override
		public void run() {
			String importantInfo[] = {
					"I'm here.", 
					"Here's uni.",
					"Let's go out.",
					"Internet is nice."
			};
			
			try {
				for ( String message : importantInfo ) {
					Thread.sleep(4000);
					threadMessage(message);
				}
			}
			catch ( InterruptedException e ) {
				threadMessage("I wasn't done!");
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		long patience = 1000 * 5;
		threadMessage("Starting MessageLoop thread");
		long startTime = System.currentTimeMillis();
		Thread t = new Thread( new MessageLoop() );
		t.start();
		threadMessage("Waiting for MessageLoop to finish");
		
		while(t.isAlive()) {
			threadMessage("Still waiting...");
			t.join(1000);
			
			if ( (System.currentTimeMillis() - startTime) > patience
					&& t.isAlive() ) {
				threadMessage("Tired of waiting!");
				t.interrupt();
				t.join();
			}
		}
		threadMessage("Finally!");
	}
}












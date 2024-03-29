package nachos.threads;
import java.util.LinkedList;
public class Communicator {
	
	private static Lock mutex;
	private LinkedList<Message> Speaker;
	private LinkedList<Message> Listener;
	
	public Communicator() {
		mutex = new Lock();
		Speaker = new LinkedList<Message>();
		Listener = new LinkedList<Message>();
	}
	
	public void speak(int word) {
		mutex.acquire();
		
		if (Listener.peek() != null) {
			Message listen = Listener.removeFirst();
			listen.setMsg(word);
			listen.getCond().wake();
		}
		else {
			Message spk = new Message(word);
			Speaker.add(spk);
			spk.getCond().sleep();
		}
		
		mutex.release();
	}
	
	public int listen() {
		mutex.acquire();
		int word = 0;
		
		if (Speaker.peek() != null) {
			Message speaker = Speaker.removeFirst();
			word = speaker.getMsg();
			speaker.getCond().wake();
		}
		else {
			Message listener = new Message();
			Listener.add(listener);
			listener.getCond().sleep();
			word = listener.getMsg();
		}
		mutex.release();
		return word;
	}
	

	private class Message {
		private int msg;
		private Condition condition;

		public Message(int word) {
			msg = word;
			condition = new Condition(mutex);
		}
		public Message(){
			msg = 0;
			condition = new Condition(mutex);
		}
		
		public Condition getCond() { return condition; }
		
		public int getMsg() { return msg; }
		
		public void setMsg(int word) { msg = word; }

	}
	
/*
	//Tester method
	public static void selfTest()
	{
        System.out.println("\n**Communicator test**");
        Communicator test = new Communicator();
        
        // ** Test One **
        System.out.println("[First test: Two threads, one speaker one listener, order of speaker->listener]");
        
        KThread speakOne = new KThread(new Speaker(test, 5));
        speakOne.setName("S1");
        speakOne.fork();
        
        System.out.println("Listener should hear 5");
        KThread listenOne = new KThread(new Listener(test));
        listenOne.setName("L1"); 
        listenOne.fork();
        //speakOne.join();
        listenOne.join(); //Execute thread
        
        
        // ** Test Two **
        System.out.println("\n[Second test: Two threads, one speaker one listener, order of listener ->speaker]");
        KThread listenTwo = new KThread(new Listener(test));
        listenTwo.setName("L2");
        listenTwo.fork();
        
        System.out.println("Listener should hear 37");
        KThread speakTwo = new KThread(new Speaker(test, 37));
        speakTwo.setName("S2");
        speakTwo.fork();
        speakTwo.join();
        listenTwo.join();
        


        // ** Test Three **
        System.out.println("\n[Third test: Four threads, three speakers one listener, order of speaker*3->listener]");
        test = new Communicator();
        KThread speakThree = new KThread(new Speaker(test, 50));
        speakThree.setName("S3");
        
        KThread speakFour = new KThread(new Speaker(test, 19));
        speakFour.setName("S4");
        
        KThread speakFive = new KThread(new Speaker(test, 8));
        speakFive.setName("S5");
        
        KThread scream = new KThread(new Speaker(test, 666));
        scream.setName("Scream");
        
        speakThree.fork();
        speakFour.fork();
        speakFive.fork();
        scream.fork();
        
        System.out.println("Listener should hear 50");
        KThread listenThree = new KThread(new Listener(test));
        listenThree.setName("L3");
        listenThree.fork();
        listenThree.join();
        
        // ** Test Four **
        System.out.println("\n[Fourth test: Six threads, three speakers three listeners, order of listener*3->speaker*3]");
        Communicator atest = new Communicator();
        KThread listenFour = new KThread(new Listener(atest));
        listenFour.setName("L4");
        
        KThread listenFive = new KThread(new Listener(atest));
        listenFive.setName("L5");
        
        KThread listenSix = new KThread(new Listener(atest));
        listenSix.setName("L6");

        KThread speakSix = new KThread(new Speaker(atest, 82));
        speakSix.setName("S6");
        
        KThread speakSev = new KThread(new Speaker(atest, 99));
        speakSev.setName("S7");
        
        KThread speakEight = new KThread(new Speaker(atest, 111));
        speakEight.setName("S8");
        
        listenFour.fork();
        listenFive.fork();
        listenSix.fork();
        System.out.println("Listener should hear 82");
        speakSix.fork();
        speakSix.join();
        System.out.println("Listener should hear 99");
        speakSev.fork();
        speakSev.join();
        System.out.println("Listener should hear 111");
        speakEight.fork();
        speakEight.join();

        // ** Test 5 **
        System.out.println("\n[Fifth test: Four threads, 1 speaker 3 listeners, order of listener*3->speaker]");
        test = new Communicator();
        KThread SL9 = new KThread(new Listener(test));
        SL9.setName("SL9");

        KThread listenSev = new KThread(new Listener(test));
        listenSev.setName("L7");

        KThread listenEight = new KThread(new Listener(test));
        listenEight.setName("L8");

        KThread speakTen = new KThread(new Speaker(test, 7));
        speakTen.setName("S10");

        SL9.fork();
        listenSev.fork();
        listenEight.fork();
        System.out.println("Listener should hear 7");
        speakTen.fork();
        speakTen.join();    
        


        //}
        System.out.println("\nFinished Communicator testing");

        }
 
	//Below classes are internal classes so as not to have to mess with any external files
	//Test Speaker, holds a reference to the communicator and the message it's saying
	private static class Speaker implements Runnable
	{
		int msg;
		Communicator communicator;

		public Speaker(Communicator com, int word)
		{
			msg = word;
			communicator = com;
		}

		public void run()
		{
			communicator.speak(msg);
            System.out.println("Said: " + msg);
		}
	}

	//Test Listener, receives the message and outputs it
	private static class Listener implements Runnable
	{
		int msg;
		Communicator communicator;

		public Listener(Communicator comm)
		{
			msg = 0;
			communicator = comm;
		}

		public void run()
		{
            msg = communicator.listen();
			System.out.println("Heard: " + msg);
		}
	}
	*/

	
}

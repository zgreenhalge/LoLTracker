package Tracker;
import java.io.File;



public class Main {
	
	public static File root;
	static IOHandler io = null;
	
	public static void main(String[] args){
	    try {
	    	root = new File(System.getProperty("user.dir")+File.separator+"LoLTracker");
			if(!root.mkdir() && !root.exists())
				throw new Exception("Cannot create root directory - check write permissions");
	    	Logger.init(root);
	    	System.out.println("Checking saved files..");
	    	io = new IOHandler();
			System.out.println("Loading static data..");
			io.init();
			io.cleanUp();
			System.out.println("Goodbye!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.flush();
			Logger.loudLog(e);
			io.cleanUp();
		}
	}

	public static IOHandler getIOHandler(){
		return io;
	}
	
}

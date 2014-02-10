import java.io.File;



public class Main {
	
	public static File root;
	
	public static void main(String[] args){
		IOHandler io = null;
	    try {
	    	root = new File(System.getProperty("user.dir")+File.separator+"LoLTracker");
			if(!root.mkdir() && !root.exists())
				throw new Exception("Cannot create root directory - check write permissions");
	    	Logger.init();
	    	System.out.println("Checking saved files..");
	    	io = new IOHandler();
			System.out.println("Loading static data..");
			io.init();
			io.cleanUp();
		} catch (Exception e) {
			System.out.flush();
			Logger.loudLog(e);
			io.cleanUp();
		}
	}

}

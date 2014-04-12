package Tracker;
import GUI.MainWindow;
import java.io.File;



public class Main {
	

	private static IOHandler io = null;
        private static MainWindow window;
        private static File root;
        private static char fileSep;
	
	public static void main(String[] args){
            Logger.newUpdateScreen();
            fileSep = System.getProperty("file.seperator").charAt(0);
	    try {
	    	root = new File(System.getProperty("user.home")+File.separator+"LoLTracker");
			if(!root.mkdir() && !root.exists())
				throw new Exception("Cannot create root directory - check write permissions");
	    	Logger.init(root);
	    	Logger.loudLogLine("Checking saved files..");
	    	io = new IOHandler(root);
			Logger.loudLogLine("Loading static data..");
			io.init();
                        window = new MainWindow();
                        Logger.killUpdateScreen();
                        window.setVisible(true);
                        InputHandler.start();
			io.cleanUp();
			System.out.println("Goodbye!");
		} catch (Exception e) {
			System.out.flush();
			Logger.loudLog(e);
			io.cleanUp();
		}
	}

	/**
	 * Returns the IOHandler being used.
	 * @return IOHandler instance used by the program
	 */
	public static IOHandler getIOHandler(){
		return io;
	}
        
        /**
         * Returns the main window of the program
         * @return MainWindow instance used by the program
         */
        public static MainWindow getWindow(){
            return window;
        }
        
        /**
         * Returns the root File used by the program
         * @return File representation of the root path being used
         */
        public static File getRoot(){
            return root;
        }
	
        public static char getSeperator(){
            return fileSep;
        }
        
	/**
	 * Cleanly exit the program.
	 */
    public static void exit(){
        if(io != null)
            io.cleanUp();
        window.dispose();
        System.exit(0);
    }
}

package Tracker;

import GUI.Exclamation;
import java.io.File;

public class Updater {

	public static void main(String[] args) {
		try {
	    	File root = new File(System.getProperty("user.home")+File.separator+"LoLTracker");
			if(!root.mkdir() && !root.exists())
				throw new Exception("Cannot create root directory - check write permissions");
			Logger.init(root);
	    	Logger.newUpdateScreen();
	    	Logger.loudLogLine("Checking saved files..");
	    	IOHandler io = new IOHandler(root);
	    	io.init();
            String[] players = {"chaosrefined", "brotossjump", "celticdragon", "ginjaninja2", "honestabe1", "septimusastrum"};
            for(String name: players){
                Player.addPlayer(name);
            }
	    	Player.updateAll();
            Exclamation complete = new Exclamation("Complete!");
            Logger.killUpdateScreen();
            complete.setVisible(true);
            io.cleanUp();
            synchronized(complete.lock){
                while(complete.isVisible()){
                    complete.lock.wait();
                }
            }
		}
		catch(Exception e){
                    e.printStackTrace();
		}

	}

}

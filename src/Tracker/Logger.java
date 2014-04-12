package Tracker;
import GUI.StatusUpdater;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import Utils.Time;

/**
 * A wrapper around the Standard out streams that allows for writing logs to disk.&nbsp;
 * Provides support for an active UpdateScreen as well.&nbsp;
 * If dev mode is active, all logs will be printed indiscriminately.&nbsp; 
 * 
 * All methods are to be accessed statically, there will never be an instance of this class ANYWHERE.&nbsp;
 * 
 * @author Zach Greenhalge
 */
public class Logger {
	
    private Logger(){}

    private static String dateTime;
    private static File logFolder;
    private static File currentLog;
    private static PrintWriter out;
    private static ArrayList<String> writeOut;
    private static StatusUpdater updater;
    private static boolean dev = false;

    /**
     * Destroys the currently active UpdateScreen.
     */
    public static void killUpdateScreen(){
        updater.complete();
    }
    
    /**
     * Generates a new UpdateScreen.
     */
    public static void newUpdateScreen(){
        updater = new StatusUpdater(true);
        updater.setVisible(true);
    }
    
    /**
     * Loads the given StatusUpdater and starts it.
     * @param su - a StatusUpdater to be used by the Logger
     */
    public static void setUpdateScreen(StatusUpdater su){
    	updater = su;
    	updater.setVisible(true);
    }
        
    /**
     * Initializes the Logger with the given root.
     * 
     * @param root - File representation of the program's root path
     * @throws IOException when there are IO issues
     */
	public static void init(File root) throws IOException{
		dateTime = Time.updateCal().fileDateTime();
		logFolder = new File(root, "logs");
		currentLog = new File(logFolder, dateTime + ".txt");
		logFolder.mkdir();
		currentLog.createNewFile();
		writeOut = new ArrayList<String>();
		writeOut.add("");
	}
	
	/**
	 * Swaps the devMode, either enabling or disabling verbose output through the Logger.
	 * @return boolean value of the current status of dev mode
	 */
	public static boolean switchDevMode(){
		if(!dev)
			loudLogLine("Dev mode enabled!\n -Verbose error reporting enabled.");
		else
			loudLogLine("Dev mode disabled");
		ConfigVars.set("devMode", (dev = !dev));
		return dev;
	}
	
	/**
	 * Set the dev mode to a specific value.
	 * @param enabled - true for dev mode on, false for dev mode off
	 */
	public static void setDev(boolean enabled){
		dev = enabled;
	}
	
	/**
	 * Flushes the Standard out and error streams, and writes the Log out to file.
	 * @throws IOException when there is a write error
	 */
	public static void flush() throws IOException{
		System.out.flush();
		System.err.flush();
		writeOut();
	}
	
	/**
	 * Writes the log out to file.
	 * @throws IOException when there is a write error
	 */
	public static void writeOut() throws IOException{
		out = new PrintWriter(new BufferedWriter(new FileWriter(currentLog, true)));
		for(String s: writeOut)
			out.println(s);
		out.close();
		writeOut = new ArrayList<String>();
		writeOut.add("");
	}

	/**
	 * Silently log the exception.&nbsp;
	 * If dev mode is active, all calls are printed. 
	 * @param e - the exception to be logged
	 */
	public static void log(Exception e) {
		if(dev)
			loudLog(e);
		else{
			writeOut.add("#####################################");
			writeOut.add("Exception: " + e.getMessage());
			writeOut.add(" " + e.getLocalizedMessage());
			for(StackTraceElement ste: e.getStackTrace()){
				writeOut.add("   " + ste.toString());
			}
			writeOut.add("#####################################");
		}
	}

	/**
	 * Logs the given input, equivalent to Stream.&nbsp;print(String).&nbsp;
	 * If dev mode is active, all calls are printed.
	 * @param input - the String to be recorded
	 */
	public static void log(String input) {
		if(dev) 
                    loudLog(input);
		else
                    writeOut.get(writeOut.size()-1).concat(input);
	}
	
	/**
	 * Logs the given input as a new line.&nbsp; Equivalent to Stream.&nbsp;println(String).&nbsp;
	 * If dev mode is active, all calls are printed. 
	 * @param input - the String to be recorded
	 */
	public static void logLine(String input){
		if(dev) 
                    loudLogLine(input);
                else
                    writeOut.add(input);
	}

	/**
	 * Logs and prints the given input, equivalent to Stream.&nbsp;print(String).&nbsp;
	 * @param input - the String to be recorded
	 */
	public static void loudLog(String input){
		System.out.print(input);
                updater.print(input);
		writeOut.get(writeOut.size()-1).concat(input);
	}

	/**
	 * Logs the given input.&nbsp; This method will never print out.
	 * @param input
	 */
	public static void logInput(String input){
		writeOut.add(input);
	}
	
	/**
	 * Logs and prints the given input, equivalent to Stream.&nbsp;println(String).&nbsp;
	 * @param input - the String to be recorded
	 */
	public static void loudLogLine(String input){
		System.out.println(input);
                updater.println(input);
                writeOut.add(input);
	}
	
	/**
	 * Logs and prints the stack trace of the given exception.
	 * @param e - the Exception to be recorded
	 */
	public static void loudLog(Exception e) {
		int pos = writeOut.size();
		System.out.flush();
		System.err.flush();
		writeOut.add("#####################################");
		writeOut.add("Exception: " + e.getMessage());
		writeOut.add(" " + e.getLocalizedMessage());
		for(StackTraceElement ste: e.getStackTrace()){
			writeOut.add(ste.toString());
		}
		writeOut.add("#####################################");
		for(int i = pos; i<writeOut.size(); i++)
			System.err.println(writeOut.get(i));
		System.err.flush();
	}
	
}
package Tracker;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import Utils.Time;

public class Logger {
	
	private Logger(){}
	
	private static String dateTime;
	private static File logFolder;
	private static File currentLog;
	private static PrintWriter out;
	private static ArrayList<String> writeOut;
	private static boolean dev = false;
	
	public static void init(File root) throws IOException{
		dateTime = Time.updateCal().fileDateTime();
		logFolder = new File(root, "logs");
		currentLog = new File(logFolder, dateTime + ".txt");
		logFolder.mkdir();
		currentLog.createNewFile();
		writeOut = new ArrayList<String>();
		writeOut.add("");
	}
	
	public static boolean switchDevMode(){
		if(!dev)
			loudLogLine("Dev mode enabled!\n -Verbose error reporting enabled.");
		else
			loudLogLine("Dev mode disabled");
		ConfigVars.set("devMode", (dev = !dev));
		return dev;
	}
	
	public static void setDev(boolean enabled){
		dev = enabled;
	}
	
	public static void flush() throws Exception{
		System.out.flush();
		System.err.flush();
		writeOut();
	}
	
	public static void writeOut() throws Exception{
		out = new PrintWriter(new BufferedWriter(new FileWriter(currentLog, true)));
		for(String s: writeOut)
			out.println(s);
		out.close();
		writeOut = new ArrayList<String>();
		writeOut.add("");
	}

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

	public static void log(String input) {
		if(dev) System.out.print(input);
		writeOut.get(writeOut.size()-1).concat(input);
	}
	
	public static void logLine(String input){
		if(dev) System.out.println(input);
		writeOut.add(input);
	}

	public static void loudLog(String input){
		System.out.print(input);
		writeOut.get(writeOut.size()-1).concat(input);
	}

	public static void logInput(String input){
		writeOut.add(input);
	}
	
	public static void loudLogLine(String input){
		writeOut.add(input);
		System.out.println(input);
	}
	
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
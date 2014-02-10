import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Logger {
	
	private Logger(){}
	
	private static String dateTime;
	private static File logFolder;
	private static File currentLog;
	private static PrintWriter out;
	private static ArrayList<String> writeOut;
	
	public static void init() throws IOException{
		dateTime = Time.updateCal().fileDateTime();
		logFolder = new File(Main.root, "logs");
		currentLog = new File(logFolder, dateTime + ".txt");
		logFolder.mkdir();
		currentLog.createNewFile();
		writeOut = new ArrayList<String>();
	}
	
	public static void writeOut() throws Exception{
		out = new PrintWriter(new BufferedWriter(new FileWriter(currentLog)));
		for(String s: writeOut){
			out.println(s);
			out.flush();
		}
		out.close();
		writeOut = new ArrayList<String>();
	}

	public static void log(Exception e) {
		writeOut.add("#####################################");
		writeOut.add("Exception: " + e.getMessage());
		writeOut.add(" " + e.getLocalizedMessage());
		for(StackTraceElement ste: e.getStackTrace()){
			writeOut.add("   " + ste.toString());
		}
		writeOut.add("#####################################");
	}

	public static void log(String input) {
		writeOut.get(writeOut.size()-1).concat(input);
	}
	
	public static void logLine(String input){
		writeOut.add(input);
	}

	public static void loudLog(String string) {
		writeOut.get(writeOut.size()-1).concat(string);
		System.out.print(string);
	}

	public static void loudLogLine(String input){
		writeOut.add(input);
		System.out.println(input);
	}
	
	public static void loudLog(Exception e) {
		int pos = writeOut.size();
		System.out.flush();
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
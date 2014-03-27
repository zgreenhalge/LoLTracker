package Utils;

import java.util.Arrays;

public class StringFunctions {
	
	public static String padLeft(String s, int length, char fill){
    	if(length < 0)
    		return s;
    	char[] array = new char[length];
    	Arrays.fill(array, fill);
    	String ret = String.copyValueOf(array);
        return ret.concat(s);
    }
    
    public static String padRight(String s, int length, char fill){
    	if(length < 0)
    		return s;
    	char[] array = new char[length];
    	Arrays.fill(array, fill);
    	String ret = String.copyValueOf(array);
        return s.concat(ret);
    }
    
    public static String centerPad(String in, int pad, char fill){
    	int total = pad-in.length();
    	int lPad = total/2;
    	int rPad = total - lPad;
    	char[] left = new char[lPad];
    	char[] right = new char[rPad];
    	Arrays.fill(left, fill);
    	Arrays.fill(right, fill);
    	return String.copyValueOf(left).concat(in).concat(String.copyValueOf(right));
    }
    
	public static boolean normEq(String s1, String s2){
		s1 = s1.replace(" ", "").replace("'", "").toLowerCase();
		s2 = s2.replace(" ", "").replace("'", "").toLowerCase();
		return s1.equals(s2);		
	}
}

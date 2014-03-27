package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import Tracker.GameStat;

public class ArrayFunctions{

	public static ArrayList<GameStat> sort(ArrayList<GameStat> list){
		Object[] array = list.toArray();
		Arrays.sort(array);
		list.clear();
		for(Object obj: array)
			list.add((GameStat)obj);
		return list;
	}
	
}

package Tracker;
import java.io.File;


public class Sandbox {

	private static File temp;
	private static File root;

	public static void main(String[] args) {
		String s = System.getProperty("user.dir")+File.separator+"LoLTracker";
		root = new File(s);
		File playerDirs = new File(root, "players");
		int x=0;
		System.out.println(playerDirs.getAbsolutePath());
		for(File f: playerDirs.listFiles()){	//all player files
			System.out.println(++x);
			System.out.println(">" + f.toString());
			System.out.println("Name: " +f.getName());
			System.out.println("Parent: " + f.getParentFile());
			if(f.isDirectory())					//if is player folder
				for(File g: f.listFiles()){		//list all inside
					System.out.println(">>" + g.toString());
					if(g.isDirectory())			//if is games folder
						for(File h: g.listFiles())
							System.out.println(">>>" + h.toString());
				}
		}

	}

}

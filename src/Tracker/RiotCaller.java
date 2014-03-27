package Tracker;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;


@SuppressWarnings("unused")
public class RiotCaller {
	private long BETA_KEY = 1200;
	private long DEV_KEY = 120;
	
	/**
	 * 
	 *API RATE LIMITS
	 *500 REQUESTS/10 MINUTES
	 *10 REQUESTS/10 SECONDS
	 * 
	 **/
	
	private long REQ_WAIT = BETA_KEY;
	
	private CallArray cache = new CallArray();
	private PriorityBlockingQueue<CallStruct> callQueue = new PriorityBlockingQueue<CallStruct>();
	private KeyChain keychain = new KeyChain();
	private String[] keys = {"a1e03137-67c1-4841-8573-234d96883478", "5caf62a9-91fb-4d50-8c3a-08edda547346",
							 "62fe7564-ca52-4878-aa13-c4fde2c0835c", "fa7da0e7-54b5-48a1-89bf-3f7b3abbdb73",
							 "4a625ae3-8cbe-4450-a693-baabc9c57b74"};
	private boolean silent = true;
	
	public RiotCaller(){
		for(String k: keys)
			keychain.add(new Key(k));
	}
	
	public synchronized String getJson(String url) throws Exception{
		CallStruct old;
		if((old = cache.find(url)) == null)
			callQueue.put(new CallStruct(url));
		else return old.json;
		cache.purge();
		cache.add(callQueue.take().call(keychain.getUsableKey()));
		return cache.top().json;
	}

	public synchronized String getJson(String url, boolean silentOnce) throws Exception{
		CallStruct old;
		if((old = cache.find(url)) == null)
			callQueue.put(new CallStruct(url));
		else return old.json;
		cache.purge();
		cache.add(callQueue.take().call(keychain.getUsableKey()));
		return cache.top().json;
	}
	
	private void silentWaitForCall(Key k) throws Exception{
		synchronized(k.WAIT_LOCK){
			while(!k.usable())
				try {
					k.WAIT_LOCK.wait(REQ_WAIT/keychain.size());
				} catch (InterruptedException e) {
					throw new Exception("Connection to Riot servers failed - please try again.");
				}
		}
	}
	
	private void waitForCall(Key k) throws Exception{
		synchronized(k.WAIT_LOCK){
			System.out.print("Waiting for Riot servers");
			while(!k.usable()){
				System.out.print(".");
				try {
					k.WAIT_LOCK.wait(REQ_WAIT/keychain.size());
				} catch (InterruptedException e) {
					throw new Exception("Connection to Riot servers failed - please try again.");
				}
			}
			System.out.println();
		}
	}
	
	private class CallStruct implements Comparable<CallStruct>{
		long timeQueued;
		long timeCalled;
		String url;
		String json;

		public CallStruct(String link){
			timeQueued = System.currentTimeMillis();
			url = link;
		}
		
		public CallStruct call(Key key) throws Exception{
			timeCalled = System.currentTimeMillis();
			InputStream is;
			URL temp = new URL(url.concat(key.use()));
			try{
				is = temp.openStream();
			}
			catch(Exception e){
				if(e instanceof UnknownHostException)
					throw new Exception("Problem connecting to host - please check your internet connection");
				if(e instanceof FileNotFoundException)
					throw new Exception("404 error: " + e.getMessage());
				if(e.getMessage().contains("429")){
					if(silent) silentWaitForCall(key);
					else waitForCall(key);
				}
				else if(e.getMessage().contains("401"))
					synchronized(key.WAIT_LOCK){
						key.WAIT_LOCK.wait(1000);
					}
				this.call(key);
				return this;
			}
			Scanner s = new Scanner(is).useDelimiter("\\A");
			json = s.hasNext() ? s.next() : "";
			is.close();
			return this;
		}
		
		public boolean equals(String s){
			return url.equals(s);
		}
		
		//will sort more recent times higher into array
		@Override
		public int compareTo(CallStruct obj) {
			int dif = (int)(obj.timeCalled-timeCalled);
			if(dif != 0)
				return dif;
			else return (int)(obj.timeQueued-timeQueued);
			
		}
	}
	
	private class CallArray{
		
		private List<CallStruct> calls;
		long lastCall;
		
		public CallArray(){
			calls = new ArrayList<CallStruct>();
		}
		
		public void add(CallStruct c){
			calls.add(c);
			sort();
		}

		private void sort(){
			Object[] cArray = calls.toArray();
			Arrays.sort(cArray);
			calls = new ArrayList<CallStruct>();
			for(Object obj: cArray)
				calls.add((CallStruct)obj);
			lastCall = top().timeCalled;
		}
		
		private void purge(){
			ArrayList<CallStruct> remove = new ArrayList<CallStruct>();
			for(CallStruct cs: calls){
				if(System.currentTimeMillis() - cs.timeCalled > 600000)
					remove.add(cs);
			}
			calls.removeAll(remove);
		}
		
		public int size(){
			return calls.size();
		}
		
		public CallStruct top(){
			return calls.get(0);
		}
		
		//find a cached call
		public CallStruct find(String thisOne){
			for(CallStruct c: calls)
				if(c.url.equals(thisOne))
					return c;
			return null;
		}
	}
	
	private class KeyChain{
		
		private ArrayList<Key> chain;
		private int keyLoop;
		
		public KeyChain(){
			chain = new ArrayList<Key>();
			keyLoop = 0;
		}
		
		public void add(Key k){
			chain.add(k);
		}
		
		public int size(){
			return chain.size();
		}
		
		public Key getUsableKey() throws Exception{
			Key temp = chain.get(keyLoop);
			while(!temp.usable()){
				if(silent)silentWaitForCall(temp); 
				else waitForCall(temp);
				keyLoop = ((keyLoop+1)%(chain.size()));
				temp = chain.get(keyLoop);
			}
			return temp;
		}
		
	}
	
	private class Key{
		
		public Object WAIT_LOCK = new Object();
		private String key;
		private long lastCall;
		
		public Key(String s){
			key = s;
			lastCall = 0;
		}
		
		public boolean usable(){
			return (System.currentTimeMillis() - lastCall > REQ_WAIT);
		}
		
		public String use(){
			lastCall = System.currentTimeMillis();
			return key;
		}
		
	}
	
}

package sectorWork;

import java.util.ArrayList;

public class File {	
	

	public boolean isFile;
	public boolean isDirectory;
	public ArrayList<File> children;
	public boolean hidden;
	public String name;
	public ArrayList<Character> text;
	public int fileSize; 
	public Byte[] entry;
	
	
	
	public File(Byte[] entry){
		children =  new ArrayList<File>();
		this.entry = entry;
		name = byteToString(0, 10);
		Byte cmp11 = new Byte("10");
		if(entry[11].equals(cmp11))
		{
			isDirectory = true;
		}
		else
		{
			isDirectory = false;
		}
	}
	
	
	//takes two integers and returns a string of the bytes between those indexes
	//used for the name of the file
	private String byteToString(int begIndex, int endIndex) {
		byte[] toString = new byte[endIndex- begIndex];
		for(int i = 0; i< endIndex - begIndex; i++ )
		{
			toString[i] = entry[i];
		}
		String toReturn = new String(toString);
		return toReturn;
	}


	//endian helper methods to cast to ints 
	private int fourBytesToInt(int first, int second, int third, int fourth)
	{
		return ((entry[first] << 24) | ((entry[second] & 0x000000FF) << 16)  | ((entry[third] & 0x000000FF) << 8 ) | (entry[fourth] & 0x000000FF));
	}
	private int twoBytesToInt(int first, int second)
	{
		return (((entry[first] & 0x000000FF) << 8 ) | (entry[second] & 0x000000FF));
	}
	
	public void main()
	{
		byte sds = 0x4E;
		Byte[] test = {46, 53, 49, 0x4E, 46, 4F, 20, 20,  54, 58, 54, 20, 00, 64, BB, 36, 87, 44, 89, 4C, 00, 00, BB, 36, 87, 44, 03, 00, 60, 01, 00, 00};
	}
}

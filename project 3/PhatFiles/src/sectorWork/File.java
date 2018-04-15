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
		Byte[] test = {0x46, 0x53, 0x49, 0x4E, 0x46, 0x4F,
				0x20, 0x20,  0x54, 0x58, 0x54, 0x20, 0x00,
				0x64, 0xBB, 0x36, 0x87, 0x44, 0x89, 0x4C,
				0x00, 0x00, 0xBB, 0x36, 0x87, 0x44, 0x03,
				0x00, 0x60, 0x01, 0x00, 0x00};
	}
}

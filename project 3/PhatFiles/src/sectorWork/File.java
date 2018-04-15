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
	public int address;
	public int clusterNum;
	
	
	
	public File(Byte[] entry){
		children =  new ArrayList<File>();
		this.entry = entry;
		name = byteToString(0, 10);
		Byte cmp11 = (byte) (entry[11] & 0x10);
		if(cmp11 == (0x0))
		{
			isDirectory = false;
		}
		else
		{
			isDirectory = true;
		}
		
		clusterNum = fourBytesToInt(21, 20, 27, 26);
		fileSize = fourBytesToInt(31, 30, 29, 28);
		address = getAddress();
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
	
	//check again later
	private int getAddress(){
		int rtn = ((Fat32_reader.BPB_BytsPerSec * Fat32_reader.BPB_SecPerClus) * (clusterNum - 2) ) 
				+ Fat32_reader.rootAddr;
		return rtn;
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
				0x64, (byte) 0xBB, 0x36, (byte) 0x87, 0x44, (byte) 0x89, 0x4C,
				0x00, 0x00, (byte) 0xBB, 0x36, (byte) 0x87, 0x44, 0x03,
				0x00, 0x60, 0x01, 0x00, 0x00};
	}
}

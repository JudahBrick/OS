package sectorWork;

import java.util.ArrayList;
import java.util.Scanner;

public class MyFile {	
	
//ATTRIBUTES
	public boolean isFile;
	public boolean isDirectory;
	public boolean modified;
	public boolean readOnly;
	public boolean ATTR_Sys;
	public boolean ATTR_VOLID;
	public boolean hidden;
	public boolean longDir = false;
	public String Attr_Type;
	

	
	public ArrayList<MyFile> children;
	public String name;
	public ArrayList<Character> text =  new ArrayList<>();//if this is a file then these are the bytes in the file
	public int fileSize; 
	public byte[] entry;
	public int address;
	public int clusterNum;
	public MyFile parent;
	public ArrayList<Integer> clusNums;
	
	
	
	
	public MyFile(byte[] entry, MyFile parent){
		this.parent = parent;
		if(entry[11] == 0x0F){
			longDir = true;
			return;
		}
		children =  new ArrayList<MyFile>();
		this.entry = entry;
		name = byteToString();
		
		if(ATTR_VOLID = parseATTR(entry[11] & 0x08)){
			isDirectory = true;
			isFile = false;
		}
		else{
			isDirectory = parseATTR(entry[11] & 0x10);
			isFile = !isDirectory;
		}
		readOnly = parseATTR(entry[11] &  0x01);
		hidden = parseATTR(entry[11] & 0x02);
		ATTR_Sys = parseATTR(entry[11] & 0x04);
		modified = parseATTR(entry[11] & 0x20);
		if(parseATTR(entry[11] &  0x01))
		{
			Attr_Type = "ATTR-READ_ONLY";
		}else if(parseATTR(entry[11] & 0x02))
		{
			Attr_Type = "ATTR-HIDDEN";
		}else if(parseATTR(entry[11] & 0x04))
		{
			Attr_Type = "ATTR-SYSTEM";
		}else if(parseATTR(entry[11] & 0x08))
		{
			Attr_Type = "ATTR-VOLUME_ID";
		}else if(parseATTR(entry[11] & 0x10))
		{
			Attr_Type = "ATTR-DIRECTORY";
		}else if(parseATTR(entry[11] & 0x20))
		{
			Attr_Type = "ATTR-ARCHIVE";
		}
		
		clusterNum = fourBytesToInt(21, 20, 27, 26);
		fileSize = fourBytesToInt(31, 30, 29, 28);
		address = getAddress();
		clusNums = getClusNums();
		if(isFile){
			//clusNums = getClusNums();
			read();
		}
	}
	
	private boolean parseATTR(int num){
		if(num != 0){
			return true;
		}
		else{
			return false;
		}
		
	}
	
	//takes two integers and returns a string of the bytes between those indexes
	//used for the name of the file
	private String byteToString() {
		byte[] toString = new byte[12];
		boolean needDot = false;
		if(entry[8] > 32 && entry[8] < 126){
			needDot = true;
		}
		
		int stringCounter = 0;
		for(int i = 0; i< 12; i++ )
		{
			if(needDot && i == 8){
				toString[i] = '.';
				continue;
			}
			else if(i == 8){
				toString[i] =  ' ';
				continue;
			}
			toString[i] = entry[stringCounter];
			stringCounter++;
		}
		
		String toReturn = new String(toString);
		toReturn = toReturn.replaceAll(" ", "");
		
		return toReturn;
	}

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
	
	
	
	private ArrayList<Integer> getClusNums(){
		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(clusterNum);
		int i = 1;
		while(nums.get(i -1) != 0x0FFFFFFF && nums.get(i -1) != 0x0FFFFFF8  && nums.get(i -1) != 0xEFFF){
			int pastClus = nums.get(i -1);
			int nextClus = (pastClus * 4) + Fat32_reader.FAT;
			nextClus = fourBytesToInt(nextClus);
			nums.add(nextClus);
			i++;
		}	
		return nums;
	}
	
	private static int fourBytesToInt(int first)
	{
		return ((Fat32_reader.disk[first + 3] << 24) | ((Fat32_reader.disk[first + 2] & 0x000000FF) << 16)  | 
				((Fat32_reader.disk[first + 1] & 0x000000FF) << 8 ) | (Fat32_reader.disk[first] & 0x000000FF));
	}
	
	private void read(){
		
		//clusNums = getClusNums();
		int nextClusToRead = 1;
		boolean EOF = false;
		
		int addrStart = address;
		for(int i = 0;!EOF; i ++){
			
			if(i == (Fat32_reader.BPB_BytsPerSec * Fat32_reader.BPB_SecPerClus)){
				i = 0;
				int nextAddr = clusNums.get(nextClusToRead);
				nextClusToRead++;
				if(nextAddr == 0x0FFFFFF8 || nextAddr == 0x0FFFFFFF){
					break;
				}
				nextAddr = ((nextAddr-2) * (Fat32_reader.BPB_BytsPerSec * Fat32_reader.BPB_SecPerClus)) + Fat32_reader.rootAddr;
				addrStart = nextAddr;
			}
			text.add((char) Fat32_reader.disk[addrStart + i]);

		}
	}
}

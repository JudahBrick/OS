package sectorWork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.File;
//import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Fat32_reader {

	static int MAX_CMD = 80;
	static byte[] disk;
	
	static int BPB_BytsPerSec;
	static int BPB_SecPerClus;
	static int clusterSize;
	static int rootAddr;
	static int FAT;
	static MyFile root;
	static String volumeID;
	private static List<Integer> freeList;
	public static int FATsz;
	
	public static void main(String[] args){
		//makeFile("", 5);
		
		/* Parse args and open our image file */

		//MyFile diskMyFile = new MyFile("/Users/yehudabrick/COMPSCI/OS/project 3/fat32.img");
		//Path diskPath = Paths.get("/Users/yehudabrick/COMPSCI/OS/project 3/fat32.img");
		String filename = new File ("").getAbsolutePath();
		System.out.println(filename);
		///src
		Path diskPath = Paths.get(filename + "/src/sectorWork/fat32.img");
		//Path diskPath = Paths.get(filename + "/sectorWork/fat32.img");
		try {
			disk = Files.readAllBytes(diskPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int first = disk[12];
		first = first << 8;
		BPB_BytsPerSec = first | disk[11];
		BPB_SecPerClus = disk[13];
		
		int BPB_RootClus = ((disk[47] << 24) | (disk[46] << 16)  | (disk[45] << 8 ) | (disk[44]));

		
		int BPB_ResvdSectCnt = (disk[15] & 0x000000FF) << 8 | (disk[14] & 0x000000FF);
		int BPB_NumFATs = disk[16];
		FATsz = ((disk[39] << 24) | ((disk[38] & 0x000000FF) << 16)  | ((disk[37] & 0x000000FF) << 8 ) | (disk[36] & 0x000000FF));
		int BPB_RootEntCnt =  disk[18] << 8 | disk[17];
		int RootDirSectors;
		int TotSec = ((disk[35] << 24) | ((disk[34] & 0x000000FF) << 16)  | ((disk[33] & 0x000000FF) << 8 ) | (disk[32] & 0x000000FF));
		;
		
		
		RootDirSectors = ((BPB_RootEntCnt * 32) + (BPB_BytsPerSec -1)) / BPB_BytsPerSec;
		
		int FirstDataSector = BPB_ResvdSectCnt + (BPB_NumFATs * FATsz) + RootDirSectors;
		
		int FirstSectorofCluster = ((BPB_RootClus - 2) * BPB_SecPerClus) + FirstDataSector;
		
		int DataSec = TotSec - (BPB_ResvdSectCnt + (BPB_NumFATs * FATsz) + RootDirSectors);
		
		int CountofClusters = DataSec / BPB_SecPerClus;
		
		int FATOffset = BPB_RootClus * 4;
		
		int ThisFATSecNum = BPB_ResvdSectCnt + (FATOffset / BPB_BytsPerSec);
		int ThisFATEntOffset = FATOffset % BPB_BytsPerSec;

		rootAddr = FirstSectorofCluster * BPB_BytsPerSec;
		
		FAT = BPB_BytsPerSec * BPB_ResvdSectCnt;
		
		clusterSize = BPB_BytsPerSec * BPB_SecPerClus;


		byte[] rootDirEntry = getDirEntry(rootAddr);
		root = new MyFile(rootDirEntry, null);
		//stat(root);

		root.children = parseRoot(rootAddr + 32);

		volumeID = root.name;
		
		//parse fat for free list
		freeList = new ArrayList<Integer>();
		parseFreelist();

		/* Parse boot sector and get information */

		/* Get root directory address */


		/* Main loop. */

		
		boolean cont = true;
		while(cont) {
			//cmdLine = new char[MAX_CMD];
			System.out.println(root.name + " /]");
			//fgets(cmd_line,MAX_CMD,stdin);
			Scanner  scan = new Scanner(System.in);
			String line = scan.nextLine();
			if(line.length() > MAX_CMD){
				throw new IllegalArgumentException("Cannot exceed 80 charecters in command prompt");
			}
			String[] words = line.split(" "); 

			/* Start comparing input */
			switch(words[0]){
			
			case "info":
				System.out.println("Going to display info!\n");
				info();
				break;
				
			case "open":
				System.out.println("Going to open!");
				break;
				
			case "close":
				System.out.println("Going to close!");
				break;
				
			case "size":
				if(words.length < 2){
					System.out.println("Must give a directory name" );
					break;
				}
				System.out.println("Going to size!");
				size(words[1]);
				break;
				
			case "volume":
				System.out.println("Going to volume!");
				System.out.println(volumeID);
				break;
			case "cd":
				if(words.length < 2){
					System.out.println("Must give a directory name" );
					break;
				}
				System.out.println("Going to cd!" );
				cd(words[1]);
				break;
				
			case "ls":
				if(words.length < 2){
					System.out.println("Must give a directory name" );
					break;
				}
				System.out.println("Going to ls!");
				ls(words[1]);
				break;
				
			case "stat":
				if(words.length < 2){
					System.out.println("Must give a file name" );
					break;
				}
				System.out.println("Going to stat!");
				stat(words[1]);
				break;
				
			case "read":
				if(words.length < 4){
					System.out.println("Must give a file name, position to start from, and amount of bytes to be read" );
					break;
				}
				System.out.println("Going to read!\n");
				read(words[1], Integer.parseInt(words[2]), Integer.parseInt(words[3]));
				break;
			
			case "freelist":
				freeList();
				break;
				
			case "quit":
				cont = false;
				System.out.println("Goodbye!\n");
				break;
					
			default:
				System.out.println("Unrecognized command!!!!\n");
			}


		}

		/* Close the file */
/* Success */
	}
	static void info(){
		//BPB_BytsPerSec
		System.out.println(BPB_BytsPerSec);
		
		//BPB_SecPerClus
		System.out.println(BPB_SecPerClus);
		
		//BPB_RsvdSecCnt
		int first = disk[15] << 8;
		int full = first | disk[14];
		System.out.println( full);
		
		//BPB_NumFATS
		System.out.println(disk[16]);
		
		//BPB_FATSz32
		int front = disk[39] << 24;
		int second = disk[38] << 16;
		int third = disk[37] << 8;
		int fourth = disk[36] ^ 0xFFFFFF00;
		front = (front | second | third | fourth);
		System.out.println( front); 
		
	
		
	}
	
	static void ls(String fileName){
		if(fileName.equals(".")){
			ls(root);
			return;
		}
		
		else if(fileName.equals("..")){
			if(root.parent != null){
				ls(root.parent);
				return;
			}
			else{
				System.out.println("In root directory, there is no directory above this one");
				return;
			}
			
		}

		for(int i = 0; i < root.children.size(); i++){
			MyFile child = root.children.get(i);
			if(fileName.equalsIgnoreCase(child.name)){
				if(child.isDirectory){
					ls(child);
					return;

				}
				else{
					System.out.println(fileName + " is not a directory");
					return;
				}
			}
			
		}
		System.out.println(fileName + " does not exist");
		
	}
	
	
	private static void ls(MyFile file){
		for(int i = 0; i < file.children.size(); i++){
			MyFile crnt = file.children.get(i);
			if(!crnt.hidden){
				System.out.println(crnt.name);

			}
		}
	}
	
	
	//for a 4 byte sequence 0xABCD
	//1st byte is A second byte is B.....
	private int fourBytesToInt(int first, int second, int third, int fourth)
	{
		return ((disk[first] << 24) | ((disk[second] & 0x000000FF) << 16)  | ((disk[third] & 0x000000FF) << 8 ) | (disk[fourth] & 0x000000FF));
	}
	private int twoBytesToInt(int first, int second)
	{
		return (((disk[first] & 0x000000FF) << 8 ) | (disk[second] & 0x000000FF));
	}
	
	
		/*
		 * this address should be the address of the first entry and not the dirEntry
		 */
		private static ArrayList<MyFile> parseRoot(int addr){  
			boolean EOC = false;
			
			ArrayList<MyFile> children = new ArrayList<>();			
			for(int i = addr;!EOC; i += 32){
				
				byte[] dirEntry = getDirEntry(i);
				if(dirEntry == null){
					EOC = true;
					break;
				}	

				MyFile myFile = new MyFile(dirEntry, root);//
				if(myFile.longDir){
					continue;
				}
				else if(myFile.isDirectory){ 
					myFile.children = parseDir(myFile);//address here means the address of the actual directory not dirEntry
					children.add(myFile);
				}
				else { 
					children.add(myFile);
				}
			}
			return children;
			
		}
		
		private static ArrayList<MyFile> parseDir(MyFile file){  
			ArrayList<Integer> clusNums = getClusNums(file.clusterNum);
			int nextClusToRead = 1;
			boolean EOC = false;
			
			ArrayList<MyFile> children = new ArrayList<>();		
			int addrStart = file.address + 64;
			for(int i = 0;!EOC; i += 32){
				
				//if were at the end of the cluster
				if(i == (clusterSize) || 
						((i == (clusterSize) - 64) && nextClusToRead == 1)){
					i = 0;											//reset i to 0
					int nextAddr = clusNums.get(nextClusToRead);	//change next cluster number
					nextClusToRead++;
					if(nextAddr == 0x0FFFFFFF){//if the next cluster number is an end of cluster
						break;
					}
					//set the address of the cluster number
					nextAddr = ((nextAddr-2) * (clusterSize)) + rootAddr;	
					addrStart = nextAddr;
				}
				
				byte[] dirEntry = getDirEntry(addrStart + i);
				if(dirEntry == null){
					EOC = true;
					break;
				}	

				MyFile myFile = new MyFile(dirEntry, file);
				if(myFile.longDir){
					continue;
				}
				else if(myFile.isDirectory){ 
					myFile.children = parseDir(myFile);
					children.add(myFile);
				}
				else { 
					children.add(myFile);
				}
			}
			return children;
			
		}
		private static ArrayList<Integer> getClusNums(int firstClus){
			ArrayList<Integer> nums = new ArrayList<Integer>();
			nums.add(firstClus);
			int i = 1;
			while(nums.get(i -1) != 0x0FFFFFFF && nums.get(i -1) != 0x0FFFFFF8){
				int pastClus = nums.get(i -1);
				int nextClus = (pastClus * 4) + FAT;
				nextClus = fourBytesToInt(nextClus);
				nums.add(nextClus);
				i++;
			}	
			return nums;
		}
		
		
		private static byte[] getDirEntry(int addr){
			byte[] dirEntry = new byte[32];
			int counter = 0;
			for(int i = addr; i < addr + 32; i++){
				dirEntry[counter] = disk[i];
				
				// this would only be the case if there are no more files/dirs
				if(dirEntry[0] == 0 || dirEntry[0] == 0xE5){
					return null;
				}
				counter++;
			}
			return dirEntry;
		}
	private static void stat(String filename){
		if(filename.equalsIgnoreCase(root.name) || filename.equals(".")){
			
			{
				stat(root);
				return;
			}
		}
		
		else if(filename.equals("..")){
			if(root.parent != null){
				stat(root.parent);
				return;
			}
			else{
				System.out.println("There is nothing above this directory");
				return;
			}
		}
		
		for(int i = 0; i < root.children.size(); i++)
		{
			if(filename.equalsIgnoreCase(root.children.get(i).name.toString()))
			{
				stat(root.children.get(i));
				return;
			}
			
		}

		System.out.println("Error: file/directory does not exist");	
	}
	
	
	private static void stat(MyFile file){
		System.out.println("Name of File:   " +  file.name);
		System.out.println("Size of File:   " +  file.fileSize);
		System.out.println("Attributes: " + file.Attr_Type);	
		System.out.println();
		System.out.println("First cluster Number:   " +  file.clusterNum);
	}
	
	public static void cd(String fileName){
		if(fileName.equals(".")){
			System.out.println("already in " + fileName +" directory");
			return;
		}
		
		else if(fileName.equals("..")){
			if(root.parent != null){
				root = root.parent;
				return;
			}
			else{
				System.out.println("already in root directory");
				return;
			}
			
		}

		for(int i = 0; i < root.children.size(); i++){
			MyFile child = root.children.get(i);
			if(fileName.equalsIgnoreCase(child.name)){
				if(child.isDirectory){
					root = child;
					System.out.println(root.name);
					return;

				}
				else{
					System.out.println(fileName + " is not a directory");
					return;
				}
			}
			
		}
		System.out.println(fileName + " does not exist");
		
	}
	
	
	private static int fourBytesToInt(int first)
	{
		return ((disk[first + 3] << 24) | ((disk[first + 2] & 0x000000FF) << 16)  | 
				((disk[first + 1] & 0x000000FF) << 8 ) | (disk[first] & 0x000000FF));
	}
	
	private static void read(String fileNameToRead, int position, int numBytes){
		MyFile fileToRead = null;
		ArrayList<Character> file;
		for(int i = 0; i < root.children.size(); i++)
		{
			if(fileNameToRead.equalsIgnoreCase(root.children.get(i).name))
			{
				fileToRead = root.children.get(i);
				if(fileToRead.isDirectory){
					System.out.println("Cannot read a directory");
				}
				break;
			}
			
		}
		if(fileToRead != null){
			file = fileToRead.text;
		}
		else{
			System.out.println("That file does not exsist");
			return;
		}
		
		ArrayList<Character> fileToPrint = new ArrayList<>();
		
		for(int i = position; i <= position + numBytes || i < file.size(); i++){
			System.out.print(file.get(i));
		}
		System.out.println("");	
	}
	
	private static void size(String fileName){
		if(fileName.equalsIgnoreCase(root.name)){
					
					{
						System.out.println("Size of File:   " +  root.fileSize);
						return;
					}
		}
				
		for(int i = 0; i < root.children.size(); i++){
				if(fileName.equalsIgnoreCase(root.children.get(i).name.toString())){
					System.out.println("Size of File:   " +  root.children.get(i).fileSize);
					return;
				}
					
		}
		
				System.out.println("Error: file/directory does not exist");
		
	}
	
	public static void parseFreelist(){
		int index = 0;
		for(int i = FAT; i < FAT + (FATsz * BPB_BytsPerSec) ; i += 4){
			int clusNum = disk[i] | disk[i +1] | disk[i +2] | disk[i +3];
			clusNum = clusNum & 0xFF;
			if(clusNum == 0){
				freeList.add(index);
			}
			index++;
		}
		
		freeList.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
			
		});
	}
	
	public static void freeList(){
		System.out.println(freeList.get(0));
		System.out.println(freeList.get(1));
		System.out.println(freeList.get(2));
		
		for(int i =0; i < 50; i++){
			System.out.println(freeList.get(i));

		}
		System.out.println(freeList.size());

	}
	
	private static void makeFile(String filename, int size){
		int numOfClus = size /	(clusterSize);
		if(size % clusterSize != 0){ // if we need overflow into the next cluster
			numOfClus++;
		}
		
		ArrayList<Integer> clusNums = root.clusNums;
		// make sure were getting right cluster number
		Integer lastClus = root.clusNums.get(root.clusNums.size() - 2);
		int addrOfClus = lastClus * clusterSize + rootAddr;
		int checkByte = addrOfClus + (clusterSize - 32);// maybe needs to be 31?
		
		if(disk[checkByte] != 0 && disk[checkByte] != 0xE5){
			addClusToDir(lastClus);
		}
		
		lastClus = root.clusNums.get(root.clusNums.size() - 2);
		int firstClusOfFile = freeList.remove(0);
		byte[] dirEntry = makeDirEntry(filename, size, firstClusOfFile);
		enterEntry(dirEntry, lastClus);
		
		//add all the data for this file
		
		MyFile child = new MyFile(dirEntry, root);
		root.children.add(child);

		
	}
	
	private static void updateData(int firstClus, int size, int numOfClus){
		int bytesAdded = 0;
		int startAddr = firstClus * clusterSize + rootAddr;
		//New File.\r\n
		char[] word;
		int count511 = 0;
		int wordCounter = 0;
		String blh = "New File.\r\n";
		word = blh.toCharArray(); 
		
		
		
		while(bytesAdded < size){
			if(bytesAdded % clusterSize != 0)
			{
				int nextClus = freeList.remove(0);
				//update both fats 
				
				startAddr = nextClus * clusterSize + rootAddr;
				count511 = 0;
			}
			bytesAdded++;
			count511++;
			wordCounter = wordCounter++;
			wordCounter = wordCounter % word.length;
			disk[startAddr + count511] = (byte) word[wordCounter];
			
		}
		//need to keep writing everything here
		//need to keep taking off a new cluster number
		//update both fats with the new cluster numbers
		
	}
	
	private static void enterEntry(byte[] entry, int cluster){
		int addr = cluster * clusterSize + rootAddr;
		for(int i = addr; i < addr + clusterSize; i +=32){
			if( disk[i] == 0 ||  disk[i] == 0xE5){
				for(int j = 0; j < 32; j++){
					disk[i + j] = entry[j];
				}
			}
		}
	}
	
	
	private static byte[] makeDirEntry(String filename, int size, int firstClus){
		byte[] dirEntry =  new byte[32];
		String[] names = filename.split(".");
		char[] charName = names[0].toCharArray();
		int charNameSize = charName.length;
		if(charName.length > 8)
		{
			charNameSize = 8;
		}
		for(int i = 0; i < charNameSize; i++)
		{
			dirEntry[i] = (byte)charName[i];
		}
		if(names[1] != null)
		{
			char[] ext = names[1].toCharArray();
		
			int extSize = ext.length;
			if(ext.length > 3)
			{
				extSize = 3;
			}
			for(int j = 0; j < extSize; j++)
			{
				dirEntry[j + 8] = (byte)charName[j];
			}
		}
		//name done 
		byte[] fileSize = intToByteArray(size);
		for(int k = 0; k < 4; k++)
		{
			dirEntry[28 + k] = fileSize[k];
		}
		//file size done 
		byte[] clus = intToByteArray(firstClus);
		dirEntry[26] = clus[0];
		dirEntry[27] = clus[1];
		dirEntry[20] = clus[2];
		dirEntry[21] = clus[3];
		//cluster done 
		
		dirEntry[11] = 0x20;//this is the same as the CONST.txt file
		//all attributes done

		
		
		
		return dirEntry;
	}
	
	private static void addClusToDir(int lastClus){
		int location = (lastClus * 4) + FAT;
		int nextClus = freeList.remove(0);
		
		//adding the new clus num to the list of cluster numbers for the root file right before the EOF
		root.clusNums.add(root.clusNums.size() -1, nextClus);// check api for arraylist

		//disk[location] = nextClus; reveres endian 
		int nextClusLocation = (nextClus * 4) + FAT;
		//disk[nextClusLocation] = 0x0FFFFFF8; reveres endian 
		
		int locationFat2 = (lastClus * 4) + FAT + FATsz * BPB_BytsPerSec;
		//disk[locationFat2] = nextClus; reveres endian 
		int nextClusLocationFat2 = (nextClus * 4) + FAT + FATsz * BPB_BytsPerSec;
		//disk[nextClusLocationFat2] = 0x0FFFFFF8; reveres endian 
	}
	
	//returns in little endian 
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value),
	            (byte)(value >>> 8),
	            (byte)(value >>> 16),
	            (byte)( value >>> 24)};
	}
	
	
}
	


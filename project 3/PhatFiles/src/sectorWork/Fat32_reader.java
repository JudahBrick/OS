package sectorWork;

import java.util.ArrayList;
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
	static int rootAddr;
	static int FAT;
	static MyFile root;
	static String volumeID;
	
	public static void main(String[] args){
		
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
		int FATsz = ((disk[39] << 24) | ((disk[38] & 0x000000FF) << 16)  | ((disk[37] & 0x000000FF) << 8 ) | (disk[36] & 0x000000FF));
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


		byte[] rootDirEntry = getDirEntry(rootAddr);
		root = new MyFile(rootDirEntry, null);
		//stat(root);

		root.children = parseRoot(rootAddr + 32);

		volumeID = root.name;
		

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
				if(i == (BPB_BytsPerSec * BPB_SecPerClus) || 
						((i == (BPB_BytsPerSec * BPB_SecPerClus) - 64) && nextClusToRead == 1)){
					i = 0;											//reset i to 0
					int nextAddr = clusNums.get(nextClusToRead);	//change next cluster number
					nextClusToRead++;
					if(nextAddr == 0x0FFFFFFF){//if the next cluster number is an end of cluster
						break;
					}
					//set the address of the cluster number
					nextAddr = ((nextAddr-2) * (BPB_BytsPerSec * BPB_SecPerClus)) + rootAddr;	
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
}
	


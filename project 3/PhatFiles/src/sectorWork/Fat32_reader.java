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
	
	public static void main(String[] args){
		char[] cmdLine;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int first = disk[12];
		first = first << 8;
		BPB_BytsPerSec = first | disk[11];
		BPB_SecPerClus = disk[13];
		
		int BPB_RootClus = ((disk[47] << 24) | (disk[46] << 16)  | (disk[45] << 8 ) | (disk[44]));
		//rootAddr = 512;//((BPB_BytsPerSec * BPB_SecPerClus) * BPB_RootEntCnt);
		
		//MyFile directory = new MyFile("./");
		//System.out.println(directory.getAbsolutePath());
		
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
		//MyFile sd = root;
		//int dfd = 8;
		
		System.out.println("BPB_RootEntCnt:  " + BPB_RootEntCnt);
		System.out.println("BPB_BytsPerSec:  " + BPB_BytsPerSec);
		System.out.println("BPB_ResvdSectCnt:  " + BPB_ResvdSectCnt);
		System.out.println("BPB_NumFATs:  " + BPB_NumFATs);
		System.out.println("FATsz:  " + FATsz);
		System.out.println("RootDirSectors:  " + RootDirSectors);
		System.out.println("BPB_RootClus:  " + BPB_RootClus);
		System.out.println("BPB_SecPerClus:  " + BPB_SecPerClus);
		System.out.println("FirstDataSector:  " + FirstDataSector);
		System.out.println("FirstSectorofCluster: " + FirstSectorofCluster);
		System.out.println("TotSec: " + TotSec);
		System.out.println("Datasec: " + DataSec);
		System.out.println("CountofClusters: " + CountofClusters);
		System.out.println("ThisFATSecNum: " + ThisFATSecNum);
		System.out.println("ThisFATEntOffset: " + ThisFATEntOffset);
		System.out.println("FAT: " + FAT);

		System.out.println(disk[39]);
		System.out.println(disk[38]);
		System.out.println(disk[37]);
		System.out.println(disk[36]);
		
		
		/* Parse args and open our image file */

		/* Parse boot sector and get information */

		/* Get root directory address */
		//System.out.println("Root addr is 0x%x\n", root_addr);


		/* Main loop.  You probably want to create a helper function
	       for each command besides quit. */

		boolean cont = true;
		while(cont) {
			cmdLine = new char[MAX_CMD];
			System.out.println("/]");
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
				System.out.println("Going to size!");
				break;
				
			case "cd":
				System.out.println("Going to cd!" );
				cd(words[1]);
				break;
				
			case "ls":
				System.out.println("Going to ls!");
				ls();
				break;
				
			case "stat":
				System.out.println("Going to stat!");
				// add root file
				//if File name does not exsist print out an error
				// otherwise print out stuff
				stat(words[1]);// + words[2]);
				break;
				
			case "read":
				System.out.println("Going to read!\n");
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

		//return 0; /* Success */
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
		//front = ((disk[39] << 24) | (disk[38] << 16) | (disk[37] << 8) ^ fourth);
		//Integer unsigned = front;
		System.out.println( front); //TODO this doesnt work
		
	
		
	}
	
	static void ls(){
		for(int i = 0; i < root.children.size(); i++){
			MyFile crnt = root.children.get(i);
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
	
	
	//need to make the root file before we call this method
		// and then root.children = parseDir(rootAddr + 32);
		
		// this address should be the address of the first entry and not the dirEntry
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
					System.out.println("we got to here  " +  myFile.name);
					myFile.children = parseDir(myFile);//address here means the address of the actual directory not dirEntry
					children.add(myFile);
				}
				else { 
					//read data
					System.out.println("we got to here  " +  myFile.name);
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
				
				if(i == (BPB_BytsPerSec * BPB_SecPerClus) || (i == 448 && nextClusToRead == 1)){
					i = 0;
					int nextAddr = clusNums.get(nextClusToRead);
					nextClusToRead++;
					if(nextAddr == 0x0FFFFFFF){
						break;
					}
					nextAddr = ((nextAddr-2) * (BPB_BytsPerSec * BPB_SecPerClus)) + rootAddr;
					addrStart = nextAddr;
				}
				byte[] dirEntry = getDirEntry(addrStart + i);
				if(dirEntry == null){
					EOC = true;
					break;
				}	
				

				MyFile myFile = new MyFile(dirEntry, file);//
				if(myFile.longDir){
					continue;
				}
				else if(myFile.isDirectory){ 
					System.out.println("we got to here  " +  myFile.name);
					myFile.children = parseDir(myFile);//address here means the address of the actual directory not dirEntry
					children.add(myFile);
				}
				else { 
					System.out.println("we got to here  " +  myFile.name);
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
		if(filename.equalsIgnoreCase(root.name)){
			
			{
				System.out.println("Name of File:   " +  filename);
				System.out.println("Size of File:   " +  root.fileSize);
				System.out.println("Attributes: " + root.Attr_Type);	
				System.out.println();
				System.out.println("First cluster Number:   " +  root.clusterNum);
				return;
			}
		}
		
		for(int i = 0; i < root.children.size(); i++)
		{
			if(filename.equalsIgnoreCase(root.children.get(i).name.toString()))
			{
				System.out.println("Name of File:   " +  filename);
				System.out.println("Size of File:   " +  root.children.get(i).fileSize);
				System.out.println("Attributes: " + root.children.get(i).Attr_Type);	
				System.out.println();
				System.out.println("First cluster Number:   " +  root.children.get(i).clusterNum);
				return;
			}
			
		}

		System.out.println("Error: file/directory does not exist");
		//System.out.println("         Read only:   " +  dir.readOnly);
		//System.out.println("         Is Hidden:   " +  dir.hidden);
		//System.out.println("         Is an OS:   " +  dir.ATTR_Sys);
		//System.out.println("         Is Directory:   " +  dir.isDirectory);
		//System.out.println("         Has been modified:   " +  dir.modified);
		
		
		
		}
	
	public static void cd(String fileName){
		
		if(fileName.equals("..")){
			if(root.parent != null){
				root = root.parent;
				return;
			}
			else{
				System.out.println("already in root directory");
			}
			
		}

		for(int i = 0; i < root.children.size(); i++){
			MyFile child = root.children.get(i);
			if(fileName.equalsIgnoreCase(child.name)){
				if(child.isDirectory){
					root = child;
					System.out.println(root.name);

				}
				else{
					System.out.println(fileName + " is not a directory");
				}
			}
			
		}
		
	}
	
	
	private static int fourBytesToInt(int first)
	{
		return ((disk[first + 3] << 24) | ((disk[first + 2] & 0x000000FF) << 16)  | 
				((disk[first + 1] & 0x000000FF) << 8 ) | (disk[first] & 0x000000FF));
	}
	
	
	
}

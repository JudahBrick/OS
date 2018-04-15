package sectorWork;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
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
	
	public static void main(String[] args){
		char[] cmdLine;
		//File diskFile = new File("/Users/yehudabrick/COMPSCI/OS/project 3/fat32.img");
		Path diskPath = Paths.get("/Users/yehudabrick/COMPSCI/OS/project 3/fat32.img");
		//Path diskPath = Paths.get("/Users/jacobsaks/Documents/YU/2018Spring/Operating Systems/fat32.img");
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
		rootAddr = 512;//((BPB_BytsPerSec * BPB_SecPerClus) * BPB_RootEntCnt);
		
		File directory = new File("./");
		System.out.println(directory.getAbsolutePath());
		
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
		
		for(int i = rootAddr; i < rootAddr + 250; i ++){
			System.out.printf("byte num: %d  hex: %x\n", i, disk[i]);
			System.out.println("                                    char:  " + (char)disk[i]);
		}
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
				System.out.println("Going to open!\n");
				break;
				
			case "close":
				System.out.println("Going to close!\n");
				break;
				
			case "size":
				System.out.println("Going to size!\n");
				break;
				
			case "cd":
				System.out.println("Going to cd!\n");
				break;
				
			case "ls":
				System.out.println("Going to ls!\n");
				ls();
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
		System.out.println(rootAddr);
		for(int i = 0; i < 124; i ++){
			System.out.println("byte number: " + i + "    " + (char) disk[i]);
		}
		
		/*
		 * Results
		 * byte number: 71   43
			byte number: 72   48
			byte number: 73   55
			byte number: 74   43
			byte number: 75   4b
			byte number: 76   4c
			byte number: 77   45
			byte number: 78   53
			byte number: 79   20
			byte number: 80   20
			byte number: 81   20
		 */
		System.out.println(disk[rootAddr]);
		System.out.println((char)disk[rootAddr + 1]);
		System.out.println((char)disk[rootAddr + 2]);
		System.out.println((char)disk[rootAddr + 3]);
		System.out.println((char)disk[rootAddr+  4]);
		System.out.println((char)disk[rootAddr + 5]);
		System.out.println((char)disk[rootAddr + 6]);
		System.out.println((char)disk[rootAddr + 7]);
		
		

		System.out.println("total bytes  "  + disk.length);
	}
	
	
	private ArrayList<File> parseDir(int addr){
		boolean EOC = false;
		
		ArrayList<File> children = new ArrayList<>();
		
		for(int i = addr;!EOC; i += 32){
			if(disk[addr + 11] == 0x0F){
				continue;
			}
			else if(disk[addr + 11] == 0x0F){ // wtv it is we need to check for directroy
				//make file
				//set file.children = parseDir(int addr)
			}
			else if(disk[addr + 11] == 0x01){ // wtv it is we need to check for file
				//make file
				// add this file to the children
			}
		}
		return children;
		
	}
	
}

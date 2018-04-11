package sectorWork;

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
		
		int BPB_RootEntCnt = ((disk[47] << 24) | (disk[46] << 16)  | (disk[45] << 8 ) | (disk[44]));
		rootAddr = 512;//((BPB_BytsPerSec * BPB_SecPerClus) * BPB_RootEntCnt);
		
		System.out.println( BPB_RootEntCnt);


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
		for(int i = 0; i < 1224; i ++){
			System.out.printf("byte number: %d   %x\n",  i, disk[i]);
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
}

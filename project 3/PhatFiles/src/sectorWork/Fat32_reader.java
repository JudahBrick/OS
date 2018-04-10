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
	
	public static void main(String[] args){
		char[] cmdLine;
		//File diskFile = new File("/Users/yehudabrick/COMPSCI/OS/project 3/fat32.img");
		Path diskPath = Paths.get("/Users/yehudabrick/COMPSCI/OS/project 3/fat32.img");
		try {
			disk = Files.readAllBytes(diskPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(int i = 0; i < 500; i++){
			System.out.println((char)disk[i]);
		}*/


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
		int first = disk[12];
		first = first << 8;
		int full = first | disk[11]; 
		System.out.println( full);
		
		//BPB_SecPerClus
		System.out.println(disk[13]);
		
		//BPB_RsvdSecCnt
		first = disk[15] << 8;
		full = first | disk[14];
		System.out.println( full);
		
		//BPB_NumFATS
		System.out.println(disk[16]);
		
		//BPB_FATSz32
		long front = disk[39] << 32;
		int second = disk[38] << 24;
		int third = disk[37] << 16;
		front = (front | second | third | disk[36]);
		Integer unsigned = full;
		System.out.println( front); //TODO this doesnt work
		
	}
}

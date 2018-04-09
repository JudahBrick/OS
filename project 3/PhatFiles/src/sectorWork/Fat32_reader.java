package sectorWork;

public class Fat32_reader {

	static int MAX_CMD = 80;
	public static void main(String[] args){
		char[] cmdLine;


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
			//Scanner  scan = new Scanner

			/* Start comparing input */
			switch(args[0]){
			
			case "info":
				System.out.println("Going to display info.\n");
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
				break;
					
			default:
				System.out.println("Unrecognized command.\n");


		}

		/* Close the file */

		//return 0; /* Success */
	}
	}
}

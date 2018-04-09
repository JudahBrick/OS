package sectorWork;

public class Fat32_reader {

	int MAX_CMD = 80;
	public static void main(String[] args){
		char cmd_line[MAX_CMD];


		/* Parse args and open our image file */

		/* Parse boot sector and get information */

		/* Get root directory address */
		//printf("Root addr is 0x%x\n", root_addr);


		/* Main loop.  You probably want to create a helper function
	       for each command besides quit. */

		while(True) {
			bzero(cmd_line, MAX_CMD);
			printf("/]");
			fgets(cmd_line,MAX_CMD,stdin);

			/* Start comparing input */
			if(strncmp(cmd_line,"info",4)==0) {
				printf("Going to display info.\n");
			}

			else if(strncmp(cmd_line,"open",4)==0) {
				printf("Going to open!\n");
			}

			else if(strncmp(cmd_line,"close",5)==0) {
				printf("Going to close!\n");
			}
			
			else if(strncmp(cmd_line,"size",4)==0) {
				printf("Going to size!\n");
			}

			else if(strncmp(cmd_line,"cd",2)==0) {
				printf("Going to cd!\n");
			}

			else if(strncmp(cmd_line,"ls",2)==0) {
				printf("Going to ls.\n");
			}

			else if(strncmp(cmd_line,"read",4)==0) {
				printf("Going to read!\n");
			}
			
			else if(strncmp(cmd_line,"quit",4)==0) {
				printf("Quitting.\n");
				break;
			}
			else
				printf("Unrecognized command.\n");


		}

		/* Close the file */

		return 0; /* Success */
	}
	}
}

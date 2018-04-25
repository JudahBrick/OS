Jacob Saks and Judah Brick 
Project 3- FAT32 
Submission 1

There may be an issue with the path name that we provided to give the fat32.img file to the program. 
We used a relative path but for some very strange reason the relative path differed between our two computers. 

Files and Directories 

Fat32_reader.java
	this file contains the code to parse the fat32 image file and then do all necessary
	commands to access information as required. 
fat32.img
	the FAT32 image file
MyFile.java
	this is the java class which represents a file or directory in the fat32 image. 
	Each file/directory on the image is converted into a 
	java object which contains all the information such as filename, file text, size and more... 
MyTestClass.java
	we didn't really use this	


ls


stat
	stat command works when the input string is the name of the file as given
	by the ls command as explained under ls. 


to compile the program, one can use a Makeme file or use javac *.java

Programs

info- 
	gives the info of the FAT32 file

stat
	takes a filename as a parameter and returns statistics about that file 

size 
	takes a filename as a parameter and returns the size of the file 

cd
	takes a directory name and will move into that directory, 
	if the input is not a directory or no such file exists then an error will be printed

ls
	will list the files/directories in the current directory.  This will not list . and ..

read
	takes a file name, position in the file to start reading from and how many characters to read.
	This will print the requested number of characters

volume
	returns the volume name of the fat32- here it is chuckles. 

quit
	exits the shell


We had some difficulty figuring out how to correctly parse the file using the Fat Tables and Data Section. Once
we figured out how to correctly parse the file and transition to Java objects the project was quite simple and 
enjoyable. 

We used the slide decks from Florida State University that we found on the subject which helped in understanding the Fat structure. 
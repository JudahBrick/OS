Jacob Saks and Judah Brick 
Project 3- FAT32 
Submission 1

There may be an issue with the path name that we provided to give the fat32.img file to the program. 
We used a relative path but for some very strange reason the relative path differed between our two computers. 

ls
	our ls function works for the top directory, one small issue we have is that the file names are not saved with the period in them. 
	Therefore, when accessing the stat method for these files one needs to provide the name given by the ls command. 

stat
	stat command works when the input string is the name of the file as given by the ls command as explained under ls. 

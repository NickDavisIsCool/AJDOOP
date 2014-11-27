package com.silverlines.ajdoop;

import java.io.File;

public class Main {

	public static void main(String[] args) {
	
	    //Read in arguments and get things set up
	    //Make sure the user supplies all 4 arguments
	    if (args.length != (4 + 1)){
		System.err.println("Wrong number of arguments.");
		System.exit(1);
	    }
	    
	    //The name of the user program
	    String programName = args[1];
	    
	    //The input file
	    String inputFileName = args[2];
	    
	    //The script which will execute the Hadoop implementation of the problem
	    String hadoopScriptFileName = args[3];
	    
	    //The script which will execute the ajira implementation of the problem
	    String ajiraScriptFileName = args[4];
	    
	    // ---------------------------------------------------------------------
	    // ---------------------------------------------------------------------
	    // 						CALCULATE INPUT SIZE
	    
	    //Get the input file and make sure it exists
	    File directory = new File(inputFileName);
	    if (!directory.exists()){
	    	System.err.println("Input file " + inputFileName + " does not exist.");
	    	System.exit(1);
	    }
	    
	    // Calculate the size of our input
	    long size = 0;
	    for(File file : directory.listFiles()){
	    	if(file.isFile()){
	    		size += file.length();
	    	}
	    }
	    
	    // ---------------------------------------------------------------------
	    // ---------------------------------------------------------------------
	    
	    

	}

}

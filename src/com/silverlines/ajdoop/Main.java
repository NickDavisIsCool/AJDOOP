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
	    
	    //Get the input file and make sure it exists
	    File inputFile = new File(inputFileName);
	    if (!inputFile.exists()){
		System.err.println("Input file " + inputFileName + " does not exist.");
		System.exit(1);
	    }
	    
	    //For now...
	    
	    

	}

}

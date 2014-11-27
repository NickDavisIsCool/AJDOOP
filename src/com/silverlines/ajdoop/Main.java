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
	    
	    //The type of program run (eg wordcount, sort, etc.)
	    String programType = args[1];
	    
	    //The input directory
	    String inputFileName = args[2];
	    
	    //The script which will execute the Hadoop implementation of the problem
	    String hadoopScriptFileName = args[3];
	    
	    //The script which will execute the ajira implementation of the problem
	    String ajiraScriptFileName = args[4];

	    
	    // --------------------------------------------------------------------- //
	    // --------------------------------------------------------------------- //
	    // 						CALCULATE INPUT SIZE
	    //	Assumes:
	    //		1. Input args[2] is the directory where input data is taken from
	    // --------------------------------------------------------------------- //
	    
	    File directory = new File(inputFileName);
	    if (!directory.exists()){
	    	System.err.println("Input file " + inputFileName + " does not exist.");
	    	System.exit(1);
	    }
	    
	    long size = 0;
	    for(File file : directory.listFiles()){
	    	if(file.isFile()){
	    		size += file.length();
	    	}
	    }
	    
	    // --------------------------------------------------------------------- //
	    // --------------------------------------------------------------------- //
	    
	    
	    
	    
	    // --------------------------------------------------------------------- //
	    // --------------------------------------------------------------------- //
	    // 					RUN SCRIPTS, CALCULATE EXECUTION TIMES
	    //	Does:
	    //		1. Reads execution info from file
	    //			- Regression Formulas only, 0 if no formula
	    //		2. Runs AJIRA/Hadoop script based off of info
	    //			- Calculate which is better from formulas, or whichever is 0
	    //		3. Obtains run-time info from script
	    //			- Reads environment variable for execution time
	    //			- Execution Time is required to be calculated IN SCRIPT
	    //		4. Either updates file or not based on "data importance"
	    //			- how much recorded data changes regression
	    //			- Honestly, early stages could be a constant (10?)
	    // --------------------------------------------------------------------- //	    
	    

	}

}

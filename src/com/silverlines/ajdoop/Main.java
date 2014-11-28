package com.silverlines.ajdoop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Main {

	public static void main(String[] args) throws IOException {
	
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
	    // 						SPAWN PROGRAM DIRECTORY
	    //	Does:
	    //		1. Creates a directory for the program name given on input
	    //		2. Creates a dummy regression file with two lines of 1 0.
	    //			- first line is hadoop equation, second is ajira
	    // 		3. Creates 2 data files for hadoop and ajira
	    //			- first line is # of data points in file, rest is csv data
	    //	Assumes:
	    //		1. programType is a string with only [a-z][A-Z][0-9] chars
	    // --------------------------------------------------------------------- //
	    
	    File dataDir = new File("/tmp/AJDOOP_EXECUTION_DATA");
	    File probDir = new File("/tmp/AJDOOP_EXECUTION_DATA/" + programType);
	    File regInfo = new File("/tmp/AJDOOP_EXECUTION_DATA/" + programType + "/reg_info.txt");
	    File hadInfo = new File("/tmp/AJDOOP_EXECUTION_DATA/" + programType + "/hadoop_data.txt");
	    File ajiInfo = new File("/tmp/AJDOOP_EXECUTION_DATA/" + programType + "/ajira_data.txt");
	    
	    dataDir.mkdir();
	    probDir.mkdir();
	    if(!regInfo.isFile()){
	    	regInfo.createNewFile();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(regInfo.getAbsoluteFile()));
	    	bw.write("0\n0");
	    	bw.close();
	    }
	    if(!hadInfo.isFile()){
	    	hadInfo.createNewFile();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(hadInfo.getAbsoluteFile()));
	    	bw.write("0");
	    	bw.close();
	    }
	    if(!ajiInfo.isFile()){
	    	ajiInfo.createNewFile();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(ajiInfo.getAbsoluteFile()));
	    	bw.write("0");
	    	bw.close();
	    }
	  
	    
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
	    
	    BufferedReader br = new BufferedReader(new FileReader(regInfo));
	    String hadoop_reg = br.readLine();
	    String ajira_reg = br.readLine();
	    
	    if(hadoop_reg.equals("0")){
	    	//execute hadoop script, get time data, calculate regression formula, write to file
	    }
	    else if(ajira_reg.equals("0")){
	    	//execute ajira script, get time data, calculate regression formula, write to file
	    }
	    else{
	    	//take regression info, calculate most optimal, run that script, get time data, re-calculate, write to file
	    }
	    
	    //END

	}

}

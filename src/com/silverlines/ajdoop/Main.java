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

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
	
	    //Read in arguments and get things set up
	    //Make sure the user supplies all 4 arguments
	    if (args.length != 4){
	    	System.err.println("Wrong number of arguments.");
	    	System.exit(1);
	    }
	    
	    //The type of program run (eg wordcount, sort, etc.)
	    String programType = args[0];
	    
	    //The input directory
	    String inputFileName = args[1];
	    
	    //The script which will execute the Hadoop implementation of the problem
	    String hadoopScriptFileName = args[2];
	    
	    //The script which will execute the ajira implementation of the problem
	    String ajiraScriptFileName = args[3];
	    
	    String reg_fields = "intercept,data_size";
	    String data_fields = "time,data_size";

	    
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
	    //			- first line is variables, then hadoop equation, then ajira
	    // 		3. Creates 2 data files for hadoop and ajira
	    //			- just csv data
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
	    	bw.write(reg_fields + "\n0\n0");
	    	bw.close();
	    }
	    if(!hadInfo.isFile()){
	    	hadInfo.createNewFile();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(hadInfo.getAbsoluteFile()));
	    	bw.write(data_fields);
	    	bw.close();
	    }
	    if(!ajiInfo.isFile()){
	    	ajiInfo.createNewFile();
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(ajiInfo.getAbsoluteFile()));
	    	bw.write(data_fields);
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
	    String variables = br.readLine();
	    String hadoop_reg = br.readLine();
	    String ajira_reg = br.readLine();
	    int numFields = reg_fields.split(",").length;
	    double[][] x = null;
	    double[] y = null;
	    
	    OLSMultipleLinearRegression mlr = new OLSMultipleLinearRegression();
	    
	    double[] input_data = {1, (double)size};
	    int i, j;
	    
	    if(hadoop_reg.equals("0")){
	    	//execute hadoop script, get time data, calculate regression formula, write to file
	    	ProcessBuilder pb = new ProcessBuilder(hadoopScriptFileName);
	    	Process p = pb.start();
	    	p.waitFor();
	    	String time = System.getenv("EXEC_TIME");
	    	
	    	String[] reg_data = {time, "0"};
	    	String[] output_data = {time, Long.toString(size)};
	    	
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(regInfo.getAbsoluteFile()));
	    	bw.write(reg_fields + "\n");
	    	for(i = 0; i < numFields; i++){
	    		if(i == numFields -1){
	    			bw.write(reg_data[i] + "\n");
	    		}
	    		else{
	    			bw.write(reg_data[i] + ",");
	    		}
	    	}
	    	bw.write(ajira_reg);
	    	
	    	bw = new BufferedWriter(new FileWriter(hadInfo.getAbsoluteFile(), true));
	    	for(i = 0; i < numFields; i++){
	    		if(i == numFields -1){
	    			bw.write(output_data[i] + "\n");
	    		}
	    		else{
	    			bw.write(output_data[i] + ",");
	    		}
	    	}
	    }
	    else if(ajira_reg.equals("0")){
	    	//execute ajira script, get time data, calculate regression formula, write to file
	    	ProcessBuilder pb = new ProcessBuilder(ajiraScriptFileName);
	    	Process p = pb.start();
	    	p.waitFor();
	    	String time = System.getenv("EXEC_TIME");
	    	
	    	String[] reg_data = {time, "0"};
	    	String[] output_data = {time, Long.toString(size)};
	    	
	    	BufferedWriter bw = new BufferedWriter(new FileWriter(regInfo.getAbsoluteFile()));
	    	bw.write(reg_fields + "\n");
	    	bw.write(hadoop_reg + "\n");
	    	for(i = 0; i < numFields; i++){
	    		if(i == numFields -1){
	    			bw.write(reg_data[i] + "\n");
	    		}
	    		else{
	    			bw.write(reg_data[i] + ",");
	    		}
	    	}
	    	
	    	bw = new BufferedWriter(new FileWriter(ajiInfo.getAbsoluteFile(), true));
	    	for(i = 0; i < numFields; i++){
	    		if(i == numFields -1){
	    			bw.write(output_data[i] + "\n");
	    		}
	    		else{
	    			bw.write(output_data[i] + ",");
	    		}
	    	}
	    	
	    }
	    else{
	    	//take regression info, calculate most optimal, run that script, get time data, re-calculate, write to file
	    	String[] hadoop_vars = hadoop_reg.split(",");
	    	String[] ajira_vars = ajira_reg.split(",");
	    	double hadoop_sum = 0;
	    	double ajira_sum = 0;
	    	for(i = 0; i < hadoop_vars.length; i++){
	    		hadoop_sum += Double.parseDouble(hadoop_vars[i])*input_data[i];
	    		ajira_sum += Double.parseDouble(ajira_vars[i])*input_data[i];
	    	}
	    	
	    	if(hadoop_sum < ajira_sum){
		    	ProcessBuilder pb = new ProcessBuilder(hadoopScriptFileName);
		    	Process p = pb.start();
		    	p.waitFor();
		    	String time = System.getenv("EXEC_TIME");
		    	
		    	String[] output_data = {time, Long.toString(size)};
		    	
			    br = new BufferedReader(new FileReader(hadInfo));
			    br.readLine();
			    String line;
			    j = 0;
			    while((line = br.readLine()) != null){
			    	String[] tuple = line.split(",");
			    	for(i = 0; i < numFields; i++){
			    		if(i == 0){
			    			y[j] = Double.parseDouble(tuple[i]);
			    		}
			    		else{
			    			x[j][i-1] = Double.parseDouble(tuple[i]);
			    		}
			    	}
			    	j++;
			    }   
			    for(i = 0; i < numFields; i ++){
			    	if(i == 0){
			    		y[j] = Double.parseDouble(output_data[i]);
			    	}
			    	x[j][i-1] = Double.parseDouble(output_data[i]);
			    }
			    
			    mlr.newSampleData(y,x);
			    double[] params = mlr.estimateRegressionParameters();
			    
		    	String[] reg_data = {Double.toString(params[0]), Double.toString(params[1])};
		    	
		    	BufferedWriter bw = new BufferedWriter(new FileWriter(regInfo.getAbsoluteFile()));
		    	bw.write(reg_fields + "\n");
		    	for(i = 0; i < numFields; i++){
		    		if(i == numFields -1){
		    			bw.write(reg_data[i] + "\n");
		    		}
		    		else{
		    			bw.write(reg_data[i] + ",");
		    		}
		    	}
		    	bw.write(ajira_reg);

		    	bw = new BufferedWriter(new FileWriter(hadInfo.getAbsoluteFile(), true));
		    	for(i = 0; i < numFields; i++){
		    		if(i == numFields -1){
		    			bw.write(output_data[i] + "\n");
		    		}
		    		else{
		    			bw.write(output_data[i] + ",");
		    		}
		    	}
	    	}
	    	else{
		    	ProcessBuilder pb = new ProcessBuilder(ajiraScriptFileName);
		    	Process p = pb.start();
		    	p.waitFor();
		    	String time = System.getenv("EXEC_TIME");
		    	
		    	String[] output_data = {time, Long.toString(size)};
		    	
			    br = new BufferedReader(new FileReader(ajiInfo));
			    br.readLine();
			    String line;
			    j = 0;
			    while((line = br.readLine()) != null){
			    	String[] tuple = line.split(",");
			    	for(i = 0; i < numFields; i++){
			    		if(i == 0){
			    			y[j] = Double.parseDouble(tuple[i]);
			    		}
			    		else{
			    			x[j][i-1] = Double.parseDouble(tuple[i]);
			    		}
			    	}
			    	j++;
			    }   
			    for(i = 0; i < numFields; i ++){
			    	if(i == 0){
			    		y[j] = Double.parseDouble(output_data[i]);
			    	}
			    	x[j][i-1] = Double.parseDouble(output_data[i]);
			    }
			    
			    mlr.newSampleData(y,x);
			    double[] params = mlr.estimateRegressionParameters();
			    
		    	String[] reg_data = {Double.toString(params[0]), Double.toString(params[1])};	    	
		    	
		    	BufferedWriter bw = new BufferedWriter(new FileWriter(regInfo.getAbsoluteFile()));
		    	bw.write(reg_fields + "\n");
		    	bw.write(hadoop_reg + "\n");
		    	for(i = 0; i < numFields; i++){
		    		if(i == numFields -1){
		    			bw.write(reg_data[i] + "\n");
		    		}
		    		else{
		    			bw.write(reg_data[i] + ",");
		    		}
		    	}
		    	
		    	bw = new BufferedWriter(new FileWriter(ajiInfo.getAbsoluteFile(), true));
		    	for(i = 0; i < numFields; i++){
		    		if(i == numFields -1){
		    			bw.write(output_data[i] + "\n");
		    		}
		    		else{
		    			bw.write(output_data[i] + ",");
		    		}
		    	}
	    	}
	    	
	    }
	    
	    //END

	}

}

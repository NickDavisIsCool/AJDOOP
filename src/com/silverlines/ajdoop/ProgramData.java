package com.silverlines.ajdoop;

import java.util.Set;

/**
 * Represents the meta-data stored for a given problem key.
 * ProgramDatas are responsible for predicting whether a certain problem should be
 * executed using an ajira implementation or a hadoop version of the problem. To
 * make this decision, it takes records of the execution times and gathered Attributes
 * of previous problems.
 * 
 * @author Matthew
 *
 */
public final class ProgramData {

    //The key which distinguishes this specific problem
    private String problemKey;
    
    protected ProgramData(String problemKey){
	setProblemKey(problemKey);
    }

    public String getProblemKey() {
        return problemKey;
    }

    public void setProblemKey(String problemKey) {
        this.problemKey = problemKey;
    }

    /**
     * This method should be called after a program has executed with either ajira or hadoop
     * @param ajiraExecuted true if the ajira implementation was chosen, false if hadoop was chosen
     * @param attributes the set of attributes which were collected from the input file
     * @param time the execution time of the problem
     */
    public void recordExecutionData(boolean ajiraExecuted, Set<Attribute<?>> attributes, Long time){
	
    }
    
    /**
     * Decides whether or not a problem with the given attributes should be executed
     * using ajira or hadoop
     * @param attributes
     * @return true if the ajira implementation should be executed. false if hadoop should be executed
     */
    public boolean recommendAjira(Set<Attribute<?>> attributes){
	return false;
    }
    
}

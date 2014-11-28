FILES=test1
LOCAL_FILE_INPUT_DIR=/home/hduser/test_files/$FILES
LOCAL_FILE_OUTPUT_DIR=/home/hduser/output/hadoop
DFS_FILE_INPUT_DIR=/user/hduser/test_files
DFS_FILE_OUTPUT_DIR=/user/hduser/output/hadoop/$FILES
JAR=/usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.2.jar
program="wordcount"

#start-all.sh

rm -rf $LOCAL_FILE_OUTPUT_DIR/$FILES
hdfs dfs -rm -r $DFS_FILE_OUTPUT_DIR

hdfs dfs -copyFromLocal $LOCAL_FILE_INPUT_DIR $DFS_FILE_INPUT_DIR

START=$(date +%s.%N)
hadoop jar $JAR $program $DFS_FILE_INPUT_DIR/$FILES $DFS_FILE_OUTPUT_DIR

END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)

hdfs dfs -get $DFS_FILE_OUTPUT_DIR $LOCAL_FILE_OUTPUT_DIR

#stop-all.sh


#echo $DIFF

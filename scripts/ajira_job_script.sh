program="nl.vu.cs.ajira.examples.WordCount"
FILES=test1
FILE_INPUT_DIR=/home/hduser/test_files/$FILES
FILE_OUTPUT_DIR=/home/hduser/output/ajira/$FILES
LAUNCH=/home/hduser/ajira/scripts/launch.sh

rm -rf $FILE_OUTPUT_DIR

START=$(date +%s.%N)
$LAUNCH -Dibis.pool.size=1 -Dibis.serverAddress=localhost $program $FILE_INPUT_DIR  $FILE_OUTPUT_DIR

END=$(date +%s.%N)
DIFF=$(echo "$END - $START" | bc)
echo $DIFF

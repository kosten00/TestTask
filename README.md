# Test task solution.
Build with JDK 13 and Gradle 4.4.1.

Program for sorting values from multiple input files in one output file by mergin. Input files are considered to be sorted. Input files may contain one of two types of data: integers or strings. One value per line.

The result of the program is a new file sorted ascending or descending.

If the content of the source files does not allow merge sort, a partial sorting is performed by skipping incorrectly sorted input data.

All the errors and warnings are logged to standard output.

Command line arguments:

   - '-a', '-d': sorting mode (ascending or descending), optional, sort in ascending order by default;
   - '-s', '-i': data type (strings or integers), required;
   - path of the output file, required;
   - paths of the output files, atleast one;
  
Examples:

        java -jar sort-it.jar -i -a out.txt in.txt (for ascending integers)
        java -jar sort-it.jar -s out.txt in1.txt in2.txt in3.txt (for ascending strings)
        java -jar sort-it.jar -d -s out.txt in1.txt in2.txt (for descending strings)

# Build
Clone repository, in project root run in command line:
    
    gradle jar
Generated jar file location (from project root): 
    
    build/libs/sort-it.jar

# Run
    java -jar sort-it.jar

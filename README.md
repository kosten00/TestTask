# Test task solution.

Program for sorting by merging multiple files. The files are considered to be sorted.
Input files contain one of two kinds of data: integers or strings.

The result of the program is a new file sorted ascending or descending.

If the content of the source files does not allow merge sort a partial sorting is performed by skipping incorrectly sorted data.

All errors and warnings are logged to standart output.

Command line arguments:
  - '-a', '-d': sorting mode (-a or -d), optional, sort in ascending order by default;
  - '-s', '-i': data type (strings or integers), required;
  - path of the output file
  - paths of output files

Examples:

        sort-it.exe -i -a out.txt in.txt (for ascending integers)
        sort-it.exe -s out.txt in1.txt in2.txt in3.txt (for ascending lines)
        sort-it.exe -d -s out.txt in1.txt in2.txt (for descending lines)

# Building project
Clone repository, in poject root run:
    
    gradle jar.
Generated jar file location: 
    
    build/libs/sort-it.jar.

# Run
    java -jar sort-it.jar

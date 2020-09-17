# Test task solution.

Program for sorting by merging multiple files. The files are considered to be sorted.
Input files contain one of two kinds of data: integers or strings.

The result of the program is a new file sorted ascending or descending.

If the content of the source files does not allow merge sort a partial sorting is performed by skipping incorrectly sorted data.

All errors and warnings are logged to standart output.

All possible types of errors must be handled. The program should not "crash". If a
after an error, execution is impossible, the program must report this
the user indicating the reason for the failure. Partial processing if there are more than
preferable to stopping the program. The program code must be "clean".
For implementation, you can use any programming language from the list:
Java, C ++, C #, Python.
The decision is made in the form of the project source code.
Program parameters are executed at startup via command line arguments, in order:
1. sorting mode (-a or -d), optional, sort in ascending order by default;
2. data type (-s or -i), required;
3. the name of the output file, required;
4. other parameters are the names of input files, at least one.
Examples of starting from the command line for Windows:
sort-it.exe -i -a out.txt in.txt (for ascending integers)
sort-it.exe -s out.txt in1.txt in2.txt in3.txt (for ascending lines)
sort-it.exe -d -s out.txt in1.txt in2.txt (for descending lines)
The solution must be accompanied by a launch instruction. It can also display
implementation features not specified in the assignment.


Command line arguments:
  - '-a', '-d': ascending or descending sorting order.
  - '-s', '-i': type of file content, integer numbers or strings.
  - path of the output file
  - paths of output files

# Building project
Clone repository, in poject root run:
    gradle jar.
Generated jar file location: 
    build/libs/sort-it.jar.

# Run
    java -jar sort-it.jar

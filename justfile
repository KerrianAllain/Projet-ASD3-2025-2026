SRC_DIR := "src"
BIN_DIR := "bin"

# Delete compiled files directory
clean:
    @rm -rf {{BIN_DIR}}

# Compile all source files
compile:
    @javac --release 8 -Xlint:-options -d {{BIN_DIR}} {{SRC_DIR}}/*.java

# Run the Main file with the given IMAGE
runImage IMAGE: compile
    @java -classpath {{BIN_DIR}} Main {{IMAGE}}

runCompression IMAGE METHOD PARAMETER: compile
    @java -classpath {{BIN_DIR}} Main {{IMAGE}} {{METHOD}} {{PARAMETER}}

SRC_DIR := "src"
BIN_DIR := "bin"

default: clean run

# Delete compiled files directory
clean:
    @rm -rf {{BIN_DIR}}

# Compile all source files
compile:
    @javac --release 8 -Xlint:-options -d {{BIN_DIR}} {{SRC_DIR}}/*.java

# Run the Main file without any argument
run: compile
    @java -classpath {{BIN_DIR}} Main

# Run the Main file with the given IMAGE
runImage IMAGE: compile
    @java -classpath {{BIN_DIR}} Main {{IMAGE}}

# Run the Main file with the given IMAGE, a compression METHOD and its PARAMETER
runCompression IMAGE METHOD PARAMETER: compile
    @java -classpath {{BIN_DIR}} Main {{IMAGE}} {{METHOD}} {{PARAMETER}}

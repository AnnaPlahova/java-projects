ifeq ($(OS),Windows_NT)
  GRADLE := gradlew.bat
else
  GRADLE := ./gradlew
endif

LIB_DIR := lib
CLASSES := build/classes/java/main
MAIN_CLASS := org.example.graphs.s21_cli.Main

.PHONY: all clean test s21_graph s21_graph_algorithms run style

all: s21_graph s21_graph_algorithms

s21_graph:
	$(GRADLE) compileJava
	mkdir -p $(LIB_DIR)
	jar cf $(LIB_DIR)/s21_graph.jar -C $(CLASSES) org/example/graphs/s21_graph

s21_graph_algorithms: s21_graph
	mkdir -p $(LIB_DIR)
	jar cf $(LIB_DIR)/s21_graph_algorithms.jar -C $(CLASSES) org/example/graphs/s21_graph_algorithms
	jar cf $(LIB_DIR)/s21_stack.jar -C $(CLASSES) org/example/graphs/s21_stack
	jar cf $(LIB_DIR)/s21_queue.jar -C $(CLASSES) org/example/graphs/s21_queue

test:
	$(GRADLE) test

style:
	$(GRADLE) spotlessCheck

clean:
	$(GRADLE) clean
	rm -rf $(LIB_DIR)

run: all
	java -Dfile.encoding=UTF-8 -cp $(CLASSES) $(MAIN_CLASS)

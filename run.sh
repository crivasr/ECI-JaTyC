#!/bin/bash
JaTycPath="$HOME/Repos/java-typestate-checker/dist"
java -jar $JaTycPath/checker/checker.jar -classpath $JaTycPath/jatyc.jar -processor jatyc.JavaTypestateChecker *.java

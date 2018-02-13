#!/bin/sh

gradle build
clear
cd ./cobspec
java -jar `find . -name "*.jar"` $*
#!/bin/sh

set -e
gradle build
clear
cd ./cobspec
java -jar `find . -name "*.jar"` $*

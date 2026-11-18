#!/bin/zsh
# Dựng lại UseCase.asta, GeneralClass.asta, DetailClass.asta và png vào thư mục ./out
# Cần: JDK 21 và Astah UML trong ~/Applications.
cd "$(dirname "$0")"
J=/opt/homebrew/opt/openjdk@21/bin
A="$HOME/Applications/astah UML.app/Contents/Java"
CP="$A/astah-api.jar:$A/astah-uml.jar:$A/lib/*"
mkdir -p build && $J/javac -nowarn -cp "$CP" -d build BuildAstahDiagrams.java || exit 1
$J/java -Djava.awt.headless=true -cp "build:$CP" BuildAstahDiagrams "$PWD/out" 2>&1 | grep -v "<INFO>"

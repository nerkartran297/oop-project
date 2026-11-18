#!/bin/zsh
# Biên dịch và chạy chương trình (cần JDK 17+). Dùng: ./run.sh        chạy ứng dụng
#                                                     ./run.sh test   chạy bộ test thuật toán
cd "$(dirname "$0")" || exit 1
JAVA_BIN="${JAVA_HOME:+$JAVA_HOME/bin/}"
[ -x /opt/homebrew/opt/openjdk@21/bin/javac ] && JAVA_BIN=/opt/homebrew/opt/openjdk@21/bin/
OUT=build
mkdir -p "$OUT"
"${JAVA_BIN}javac" -d "$OUT" src/model/*.java src/view/*.java src/controller/*.java src/Main.java test/SortTests.java || exit 1
if [ "$1" = "test" ]; then
  "${JAVA_BIN}java" -cp "$OUT" SortTests
else
  "${JAVA_BIN}java" -cp "$OUT" Main
fi

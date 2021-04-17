echo off
java -server -Xms3G -Xmx8G -XX:+UseConcMarkSweepGC -Dfile.encoding=UTF-8 -jar launcher-1.8.8-R0.1-SNAPSHOT.jar nogui
pause
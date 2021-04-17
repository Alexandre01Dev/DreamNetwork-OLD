echo off
java -server -Xms1G -Xmx2G -XX:+UseConcMarkSweepGC -Dfile.encoding=UTF-8 -jar bungeecord-1.8.jar nogui
pause
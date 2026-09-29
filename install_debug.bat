@echo off
title POS PIK - Install Debug APK
echo Setting JAVA_HOME to Android Studio JBR...
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Running gradlew installDebug...
call gradlew.bat installDebug
pause

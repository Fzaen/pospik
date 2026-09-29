@echo off
title POS PIK - Build Release APK
echo Setting JAVA_HOME to Android Studio JBR...
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo Running gradlew assembleRelease...
call gradlew.bat assembleRelease
echo ========================================
echo APK Release built successfully at:
echo app\build\outputs\apk\release\app-release.apk
echo ========================================
pause

@echo off

set "CURRENT_DIR=%~dp0"

java -jar -Dapp.home=%CURRENT_DIR% "%~dp0\..\lib\mantenimiento-productos-1.0.jar"

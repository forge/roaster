@REM ----------------------------------------------------------------------------
@REM Copyright 2012 Red Hat, Inc. and/or its affiliates.
@REM
@REM Licensed under the Eclipse Public License version 1.0, available at
@REM http://www.eclipse.org/legal/epl-v10.html
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Roaster Startup script
@REM
@REM Required Environment vars:
@REM ------------------
@REM JAVA_HOME - location of a JRE home dir
@REM
@REM Optional Environment vars
@REM ------------------
@REM ROASTER_HOME - location of Roaster's installed home dir
@REM ROASTER_OPTS - parameters passed to the Java VM when running Roaster
@REM ----------------------------------------------------------------------------

@echo off

@REM set %USERHOME% to equivalent of $HOME
if not "%USERHOME%" == "" goto OkUserhome
set "USERHOME=%USERPROFILE%"

if not "%USERHOME%" == "" goto OkUserhome
set "USERHOME=%HOMEDRIVE%%HOMEPATH%"

:OkUserhome

@REM Execute a user defined script before this one
if exist "%USERHOME%\roasterrc_pre.bat" call "%USERHOME%\roasterrc_pre.bat"

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkJVersion

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:chkJVersion
set PATH="%JAVA_HOME%\bin";%PATH%

for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
   set JAVAVER=%%g
)
for /f "delims=. tokens=1-3" %%v in ("%JAVAVER%") do (
   set JAVAVER_MINOR=%%w
)

if %JAVAVER_MINOR% geq 7 goto chkFHome

echo.
echo A Java 1.7 or higher JRE is required to run Roaster. "%JAVA_HOME%\bin\java.exe" is version %JAVAVER%
echo.
goto error

:chkFHome
if not "%ROASTER_HOME%"=="" goto valFHome

if "%OS%"=="Windows_NT" SET "ROASTER_HOME=%~dp0.."
if "%OS%"=="WINNT" SET "ROASTER_HOME=%~dp0.."
if not "%ROASTER_HOME%"=="" goto valFHome

echo.
echo ERROR: ROASTER_HOME not found in your environment.
echo Please set the ROASTER_HOME variable in your environment to match the
echo location of the Roaster installation
echo.
goto error

:valFHome

:stripFHome
if not "_%ROASTER_HOME:~-1%"=="_\" goto checkFBat
set "ROASTER_HOME=%ROASTER_HOME:~0,-1%"
goto stripFHome

:checkFBat
if exist "%ROASTER_HOME%\bin\roaster.bat" goto init

echo.
echo ERROR: ROASTER_HOME is set to an invalid directory.
echo ROASTER_HOME = "%ROASTER_HOME%"
echo Please set the ROASTER_HOME variable in your environment to match the
echo location of the Roaster installation
echo.
goto error
@REM ==== END VALIDATION ====

@REM Initializing the argument line
:init
setlocal enableextensions enabledelayedexpansion
set ROASTER_CMD_LINE_ARGS=
set ROASTER_DEBUG_ARGS=

if "%1"=="" goto initArgs

set "args=%*"
set "args=%args:,=:comma:%"
set "args=%args:;=:semicolon:%"

for %%x in (%args%) do (
    set "arg=%%~x"
    set "arg=!arg::comma:=,!"
    set "arg=!arg::semicolon:=;!"
    if "!arg!"=="--debug" set ROASTER_DEBUG_ARGS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
    set "ROASTER_CMD_LINE_ARGS=!ROASTER_CMD_LINE_ARGS! "!arg!""
)

:initArgs
setlocal enableextensions enabledelayedexpansion
if %1a==a goto endInit

shift
goto initArgs
@REM Reaching here means variables are defined and arguments have been captured
:endInit

SET ROASTER_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTCWJars

goto runRoaster

@REM Start Roaster
:runRoaster

set ROASTER_MAIN_CLASS=org.jboss.forge.roaster.Bootstrap
%ROASTER_JAVA_EXE% %ROASTER_DEBUG_ARGS% %ROASTER_OPTS% "-Droaster.standalone=true" "-Droaster.home=%ROASTER_HOME%" ^
   -cp ".;%ROASTER_HOME%\lib\*;%ROASTER_HOME%" %ROASTER_MAIN_CLASS% %ROASTER_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT
if "%OS%"=="WINNT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set ROASTER_JAVA_EXE=
set ROASTER_CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal & set ERROR_CODE=%ERROR_CODE%

:postExec
if exist "%USERHOME%\roasterrc_post.bat" call "%USERHOME%\roasterrc_post.bat"

if "%ROASTER_TERMINATE_CMD%" == "on" exit %ERROR_CODE%

cmd /C exit /B %ERROR_CODE%



@ECHO OFF

REM Gwen workspace
REM ==============

REM ---------------------------------------------------------------------------
REM  Gwen workspace script for windows environments
REM  ---------------------------------------------------------------------------

REM
SETLOCAL enabledelayedexpansion

REM Defaults
SET DEFAULT_BROWSER=chrome
SET DEFAULT_ENV=local

SET GWEN_ARGS=%*
SET ARG1=%1
SET ARG2=%2

REM Display usage if first argument is help
IF "%1%" == "help" (
  CALL :HELPGWEN
  EXIT /B 1
)

REM Default to chrome if no target browser was provided
IF NOT "%1" == "chrome" (
  IF NOT "%1" == "firefox" (
    IF NOT "%1" == "ie" (
      IF NOT "%1" == "safari" (
        SET TARGET_BROWSER=%DEFAULT_BROWSER%
      )
    )
  )
)

REM Capture target browser if provided
IF "Z%TARGET_BROWSER%" == "Z" (
  SET TARGET_BROWSER=%1
  SHIFT
)

REM Set target environment
IF NOT "%1" == "%DEFAULT_ENV%" (
  IF NOT EXIST "env\%1.properties" (
    SET TARGET_ENV=%DEFAULT_ENV%
  ) ELSE (
    SET TARGET_ENV=%1
  )
) ELSE (
  SET TARGET_ENV=%1
)

ECHO Target browser is %TARGET_BROWSER%
ECHO Target environment is %TARGET_ENV%

REM Prepare gwen JVM arguments and properties
SET GWEN_JVM_ARGS=-Dgwen.web.browser=%TARGET_BROWSER%
SET GWEN_PROPERTIES=gwen.properties
IF NOT "%TARGET_ENV%" == "local" (
  SET GWEN_PROPERTIES=%GWEN_PROPERTIES%,env\%TARGET_ENV%.properties
)

REM Install gwen-web
SET GWEN_WEB=target\gwen-packages\gwen-web
java -jar gwen-gpm.jar -p "%GWEN_PROPERTIES%" install gwen-web gwen.gwen-web.version %GWEN_WEB%
SET EXITCODE=!ERRORLEVEL!
IF !EXITCODE! EQU 1 (
  ECHO Failed to auto-install gwen-web
  EXIT /B !EXITCODE!
)

REM Install chrome web driver if target browser is chrome
IF "%TARGET_BROWSER%" == "chrome" (
  java -jar gwen-gpm.jar -p "%GWEN_PROPERTIES%" install chrome-driver gwen.chrome-driver.version target\gwen-packages\chrome-driver
  SET EXITCODE=!ERRORLEVEL!
  IF !EXITCODE! EQU 1 (
    ECHO Failed to auto-install chrome web driver
    EXIT /B !EXITCODE!
  )
  SET "GWEN_JVM_ARGS=%GWEN_JVM_ARGS% -Dwebdriver.chrome.driver=target\gwen-packages\chrome-driver\chromedriver.exe"
)

REM Install gecko web driver if target browser is firefox
IF "%TARGET_BROWSER%" == "firefox" (
  java -jar gwen-gpm.jar -p "%GWEN_PROPERTIES%" install gecko-driver gwen.gecko-driver.version target\gwen-packages\gecko-driver
  SET EXITCODE=!ERRORLEVEL!
  IF !EXITCODE! EQU 1 (
    ECHO Failed to auto-install gecko ^(firefox^) web driver
    EXIT /B !EXITCODE!
  )
  SET "GWEN_JVM_ARGS=%GWEN_JVM_ARGS% -Dwebdriver.gecko.driver=target\gwen-packages\gecko-driver\geckodriver.exe"
)

REM Install IE driver if target browser is IE
IF "%TARGET_BROWSER%" == "ie" (
  java -jar gwen-gpm.jar -p "%GWEN_PROPERTIES%" install ie-driver gwen.ie-driver.version target\gwen-packages\ie-driver
  SET EXITCODE=!ERRORLEVEL!
  IF !EXITCODE! EQU 1 (
    ECHO Failed to auto-install IE web driver
    EXIT /B !EXITCODE!
  )
  SET "GWEN_JVM_ARGS=%GWEN_JVM_ARGS% -Dwebdriver.ie.driver=target\gwen-packages\ie-driver\IEDriverServer.exe"
)

REM If gwen.selenium.version is set to a specific version, then install that
REM selenium-java API version and set SELENIUM_HOME to the installed location
REM Otherwise do nothing if gwen.selenium.version=provided (exit code 2).
java -jar gwen-gpm.jar -p "%GWEN_PROPERTIES%" install selenium gwen.selenium.version target\gwen-packages\selenium
SET EXITCODE=!ERRORLEVEL!
IF !EXITCODE! EQU 0 (
    SET SELENIUM_HOME=target\gwen-packages\selenium
)
IF !EXITCODE! NEQ 0 (
  SET "SELENIUM_HOME="
)
IF !EXITCODE! EQU 1 (
  ECHO "Failed to auto-install selenium Java package"
  EXIT /B !EXITCODE!
)

REM Remove browser & env from Gwen program args
IF "%ARG1%" == "%TARGET_BROWSER%" (
  IF "%ARG2%" == "%TARGET_ENV%" (
    FOR /F "tokens=2* delims= " %%A in ("%*") DO SET GWEN_ARGS=%%B
  ) ELSE (
    FOR /F "tokens=1* delims= " %%A in ("%*") DO SET GWEN_ARGS=%%B
  )
) ELSE (
  IF "%ARG1%" == "%TARGET_ENV%" (
    FOR /F "tokens=1* delims= " %%A in ("%*") DO SET GWEN_ARGS=%%B
  )
)

REM Launch Gwen
ECHO
ECHO Launching Gwen
CALL %GWEN_WEB%\bin\gwen-web %GWEN_JVM_ARGS% -m meta -r target/reports -p "%GWEN_PROPERTIES%" %GWEN_ARGS%
EXIT /B !ERRORLEVEL!

:HELPGWEN
ECHO Usage^:
ECHO gwen ^[browser^] ^[env^] ^[options^] ^[features^]
ECHO     ^[browser^] =
ECHO       chrome  ^: to use chrome browser (default)
ECHO       firefox ^: to use firefox browser
ECHO       ie      ^: to use IE browser
ECHO       safari  ^: to use safari browser
ECHO      ^[env^]    =
ECHO         local ^: to use local user environment
ECHO          name ^: name of environment to use
ECHO            ^(dev will load env\dev.properties^)
ECHO      ^[options^] =
ECHO      --version
ECHO               ^: Prints the implementation version
ECHO      -b --batch
ECHO               ^: Batch/server mode
ECHO      --parallel
ECHO               ^: Parallel batch execution mode
ECHO      -f, --formats ^<formats^>
ECHO               ^: Comma separated report formats to produce.
ECHO                 Supported formats include: html slideshow junit json
ECHO                 ^(default is html^)
ECHO      -t, --tags ^<tags^>
ECHO               ^: Comma separated list of @include or ~@exclude tags
ECHO      -n, --dry-run
ECHO               ^: Check syntax and validate only
ECHO      -i, --input-data ^<input data file^>
ECHO               ^: Input data ^(CSV file with column headers^)
ECHO    ^<features^> = Space separated list of feature files/directories
EXIT /B 0

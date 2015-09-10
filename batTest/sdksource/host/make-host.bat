::================================================================::
:: Copyright (c) 2010-2011 Zipline Games, Inc.
:: All Rights Reserved.
:: http://getmoai.com
::================================================================::

	@echo off
	setlocal
	set CYGWIN=nodosfilewarning
	set channelname=%1%
	set channel_lua_name=%2%
	set projectSrc=%channelname%
	set isdecode=%6%
	set subChannel=%7%
	set packageFold=%channelname%_%channel_lua_name%_%subChannel%

	set idPath=%7%
	set proPath=%7%

	if %channelname%==%channel_lua_name% (
		
		set projectSrc=build_%channelname%
		set idPath=%channelname%


		) else (
		if "%subChannel%"=="" (

		set projectSrc=build_%channelname%_%channel_lua_name%
		set idPath=%channelname%_%channel_lua_name%

		) else (
		set projectSrc=build_%channelname%_%channel_lua_name%_%subChannel%
		)
		)

:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
set proPath=%cd%\%projectSrc%\project
set mmXmlPath=%cd%\mmXml\%idPath%

if "%isdecode%" == "true" (
rd /s /q %cd%\%projectSrc%\

if not exist %projectSrc%\project\assets md %projectSrc%\project\assets


call getChannelId %proPath% %idPath% %mmXmlPath%
)
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


	adb kill-server
	bash run-host.sh %*

	if %ERRORLEVEL% == 2 (
		set install_cmd=ant release
		set ERRORLEVEL=0
	) else (
		set install_cmd=ant debug
	)
	
	if %ERRORLEVEL% == 0 (
		pushd %projectSrc%\project
 			call ant clean
 			call %install_cmd%
		popd
		if not exist _packages\%packageFold%\ md _packages\%packageFold%\		 
		copy %projectSrc%\project\bin\*-debug.apk _packages\%packageFold%\


		if "%isdecode%" == "true" (
		copy %projectSrc%\project\bin\*-unsigned.apk _packages\%packageFold%\

		if "%subChannel%"=="" (
		call decodeAndResign.bat %channelname% %channel_lua_name% 1.9 guobao
		) else (
		call decodeAndResign.bat %channelname% %channel_lua_name% 1.9 guobao %subChannel%
		)

		
		) else (
		echo %isdecode%
		copy %projectSrc%\project\bin\*-release.apk _packages\%packageFold%\
		)
	)

	endlocal

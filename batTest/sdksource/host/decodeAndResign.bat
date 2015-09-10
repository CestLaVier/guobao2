
	@echo off
	setlocal
	set CYGWIN=nodosfilewarning
	
	set channel_name=%1%
	set channel_lua_name=%2%
	set version_name=%3%
	set gameName=%4%
	set subChannel=%5%
	set mymonth=%DATE:~5,2%
	set day=%date:~8,2%
	set packageFold=%channel_name%_%channel_lua_name%_%subChannel%

	echo %channel_lua_name

	set packagePath=%cd%\_packages\%packageFold%\
	set mmXmlPath=%cd%\project\%channel_name%_%channel_lua_name%
	set keyPath=%cd%\key\%channel_name%_key
	set dexPath=%cd%\channel\%channel_name%\classes.dex
	set subChannelXml=%mmXmlPath%

	if "%channel_name%"=="%channel_lua_name%" (

		mmXmlPath=%cd%\channel\%channel_name%

		if "%subChannel%"=="" (
		
		set apk_name=%gameName%_V%version_name%_AllInOne_%channel_name%_%mymonth%%day%-release-unsigned.apk
		set signPackageName=%gameName%_V%version_name%_AllInOne_%channel_name%_%mymonth%%day%-release.apk
		) else (
		set subChannelXml=%cd%\project\subChannel\%channel_name%_%channel_lua_name%\%subChannel%
		set apk_name=%gameName%_V%version_name%_AllInOne_%channel_name%_%mymonth%%day%_%subChannel%-release-unsigned.apk
		set signPackageName=%gameName%_V%version_name%_AllInOne_%channel_name%_%mymonth%%day%_%subChannel%-release.apk
		)


		echo "equal"
		) else (


		if "%subChannel%"=="" (
		
		set apk_name=%gameName%_V%version_name%_AllInOne_%channel_name%_%channel_lua_name%_%mymonth%%day%-release-unsigned.apk
		set signPackageName=%gameName%_V%version_name%_AllInOne_%channel_name%_%channel_lua_name%_%mymonth%%day%-release.apk
		) else (
		set subChannelXml=%cd%\project\subChannel\%channel_name%_%channel_lua_name%\%subChannel%
		set apk_name=%gameName%_V%version_name%_AllInOne_%channel_name%_%channel_lua_name%_%mymonth%%day%_%subChannel%-release-unsigned.apk
		set signPackageName=%gameName%_V%version_name%_AllInOne_%channel_name%_%channel_lua_name%_%mymonth%%day%_%subChannel%-release.apk
		)

		echo "unequal"
		)

	echo "++++++++++++++++++++++++++++++反编译进行中+++++++++++++++++++++++++++++++++"
	set xml1=%mmXmlPath%\CopyrightDeclaration.xml
	set xml2=%subChannelXml%\mmiap.xml
	
	echo %xml1%
	echo %xml2%
	
	if exist %xml1% (

	dos2unix %xml2%

	copy %xml1% %packagePath%
	copy %xml2% %packagePath%

	set oldPath=%cd%

	cd %packagePath%

	..\..\_tools\apktool\aapt r %packagePath%\%apk_name% CopyrightDeclaration.xml mmiap.xml
	..\..\_tools\apktool\aapt a %packagePath%\%apk_name% CopyrightDeclaration.xml mmiap.xml
	del mmiap.xml
	del CopyrightDeclaration.xml
	
		) else (

		echo "不存在MM计费文件，反编译结束！！！！"
		)
   

	if exist %dexPath% (
		
	copy %dexPath% %packagePath%
  	echo "-----------------------------------更换基地dex文件----------------------------"
	echo %dexPath%

  	..\..\_tools\apktool\aapt r %packagePath%\%apk_name% classes.dex
	..\..\_tools\apktool\aapt a %packagePath%\%apk_name% classes.dex

	del classes.dex

		) else (
		echo "-------------------------------不用删除dex文件----------------------------"
		)

		cd %oldPath%

	echo "+++++++++++++++++++++++++++反编译完成，签名中+++++++++++++++++++++++++++++++"
	jarsigner -verbose -keystore %keyPath% -storepass 3gu2015 -signedjar %packagePath%\%signPackageName% -digestalg SHA1 -sigalg MD5withRSA %packagePath%\%apk_name% 3gu
	::jarsigner -verbose -keystore %keyPath% -signedjar %packagePath%\%signPackageName% %packagePath%\%apk_name% 3gu 
	::del %packagePath%\%apk_name%
	echo "+++++++++++++++++++++++++++签名完成+++++++++++++++++++++++++++++++"


	endlocal

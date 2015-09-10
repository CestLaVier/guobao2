@echo off
set channelname=%1%
set channel_lua_name=%2%
set subChannel=%3%
set version=%4%

set mymonth=%DATE:~5,2%
set day=%date:~8,2%
set Fa_packagePath=%cd%\_packages\%channelname%_%channel_lua_name%_
set So_packagePath=%cd%\_packages\%channelname%_%channel_lua_name%_%subChannel%
set oldPath=%cd%
set keyPath=%cd%\key\%channelname%_key

set So_sourcePath=%cd%\project\subChannel\%channelname%_%channel_lua_name%\%subChannel%

set Fa_packageName=guobao_V%version%_AllInOne_%channelname%_%channel_lua_name%_%mymonth%%day%-release-unsigned.apk
set Fa_fold=guobao_V%version%_AllInOne_%channelname%_%channel_lua_name%_%mymonth%%day%-release-unsigned
set So_packageName=guobao_V%version%_AllInOne_%channelname%_%channel_lua_name%_%subChannel%_%mymonth%%day%-release.apk
set So_packageName2=guobao_%channelname%_%channel_lua_name%_%subChannel%_V%version%_AllInOne_%mymonth%%day%-release.apk

if not exist %Fa_packagePath%\%Fa_packageName% (
	echo "母包不存在"
	echo "%Fa_packagePath%\%Fa_packageName%"
	)

if not exist %So_packagePath% md %So_packagePath%

copy %Fa_packagePath%\*-unsigned.apk %So_packagePath%\
copy  %Fa_packagePath%\classes.dex  %So_packagePath%\

echo "拷贝母包完成"

xcopy %So_sourcePath% %So_packagePath% /e/c/q

echo "----------------------更改渠道-----------"
:::::::::::::::::::::::::::::::::::::::::::::getid

call getChannelId %So_packagePath% %subChannel% %cd%\mmXml\%channelname%_%channel_lua_name% true
:::::::::::::::::::::::::::::::::::::::::::::getid

echo "----------------------添加基地-电信-联通-相关文件-----------"
call makeApktool.bat %channelname% %channel_lua_name% %subChannel% %version%

cd %So_packagePath%

echo "-----------------------删除原始文件-------------------"

for /f %%a in ('dir /a-d /b  "%So_packagePath%\assets\**"') do (
	if exist assets\%%a (
		echo "删除%%a"
	del %So_packagePath%\%Fa_fold%\assets\%%a
	
	)else (

		echo "没文件！！！！"
		)

)

for /f %%a in ('dir /a-d /b  "%So_packagePath%\assets\UnicomConsume\**"') do (
	if exist assets\UnicomConsume\%%a (
	echo "删除%%a"
	del %So_packagePath%\%Fa_fold%\assets\UnicomConsume\%%a

	)else (

		echo "没联通文件！！！！"
		)
)

for /f %%a in ('dir /a-d /b  "%So_packagePath%\assets\**"') do (
	if exist assets\%%a (
	
	echo "添加%%a"
	copy %So_packagePath%\assets\%%a %So_packagePath%\%Fa_fold%\assets\
	
	)else (

		echo "没文件！！！！"
		)
)

for /f %%a in ('dir /a-d /b  "%So_packagePath%\assets\UnicomConsume\**"') do (
	if exist assets\UnicomConsume\%%a (

	echo "添加%%a"
	..\..\_tools\apktool\aapt a %So_packagePath%\%Fa_packageName% assets\UnicomConsume\%%a
	copy %So_packagePath%\assets\UnicomConsume\%%a %So_packagePath%\%Fa_fold%\assets\UnicomConsume\%%a
	)else (

		echo "没联通文件！！！！"
		)
)

cd %oldPath%
call buildApktool.bat  %channelname% %channel_lua_name% %subChannel% %version%


copy %So_packagePath%\%Fa_fold%\dist\%Fa_packageName% %So_packagePath%\%Fa_packageName%



cd %oldPath%

set mmXmlPath=%cd%\project\subChannel\%channelname%_%channel_lua_name%\%subChannel%
set xml1=%mmXmlPath%\CopyrightDeclaration.xml
set xml2=%mmXmlPath%\mmiap.xml

echo  %xml1%

copy %xml1% %So_packagePath%
copy %xml2% %So_packagePath%
echo "----------------------添加MM相关文件-----------"
cd %So_packagePath%
..\..\_tools\apktool\aapt r %So_packagePath%\%Fa_packageName%  CopyrightDeclaration.xml mmiap.xml classes.dex
..\..\_tools\apktool\aapt a %So_packagePath%\%Fa_packageName%  CopyrightDeclaration.xml mmiap.xml classes.dex

echo %keyPath%

jarsigner -verbose -keystore %keyPath% -storepass 3gu2015 -signedjar  %So_packagePath%\%So_packageName2% -digestalg SHA1 -sigalg MD5withRSA %So_packagePath%\%Fa_packageName%  3gu

rd /s /q %So_packagePath%\assets
rd /s /q %So_packagePath%\libs
del %So_packagePath%\CopyrightDeclaration.xml
del %So_packagePath%\mmiap.xml
del %So_packagePath%\%Fa_packageName%
rd /s /q %So_packagePath%\%Fa_fold%

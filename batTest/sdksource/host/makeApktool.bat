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

if not exist %Fa_packagePath%\%Fa_packageName% (
	echo "母包不存在"
	echo "%Fa_packagePath%\%Fa_packageName%"
	)

if not exist %So_packagePath% md %So_packagePath%

echo "----------------------反编译生成文件夹----------------"

cd %So_packagePath%

..\..\_tools\apktool\apktool d -f %So_packagePath%\%Fa_packageName% 
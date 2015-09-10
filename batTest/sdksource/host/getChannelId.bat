@echo off
setlocal enabledelayedexpansion
set proPath=%1
set channelName=%2%
set mmxmlPath=%3%
set issubchannel=%4%
set channelPath=%cd%\channelId\%channelName%\%channelName%.txt

set finishXmlPath=%cd%\project\%channelName%
set globalPath=%proPath%\mmiap.xml

echo proPath----%proPath%
echo channelName----%channelName%
echo mmxmlPath----%mmxmlPath%
echo channelPath----%channelPath%


if "%issubchannel%"=="true" (

echo "nimei"

copy %mmxmlPath%\mmiap.xml %proPath%
copy %mmxmlPath%\CopyrightDeclaration.xml %proPath%
copy %cd%\channelId\%channelName%\assets %proPath%\assets

for /f "tokens=*" %%a in (%channelPath%) do (

set "channelId=%%a"
sed -i "s#<channel>0000000000</channel>#<channel>!channelId!</channel>#" %globalPath%

dos2unix %globalPath%

break
	)


		) else (


copy %mmxmlPath%\mmiap.xml %proPath%
copy %mmxmlPath%\CopyrightDeclaration.xml %proPath%
copy %cd%\channelId\%channelName%\assets %proPath%\assets

if not exist %finishXmlPath% md %finishXmlPath%
copy %mmxmlPath%\mmiap.xml %finishXmlPath%
copy %mmxmlPath%\CopyrightDeclaration.xml %finishXmlPath%


for /f "tokens=*" %%a in (%channelPath%) do (

set "channelId=%%a"
echo.&echo channelId---!channelId!
echo.&echo channelId2---%globalPath%
echo.&echo channelId3---%finishXmlPath%\mmiap.xml
sed -i "s#<channel>0000000000</channel>#<channel>!channelId!</channel>#" %globalPath%
sed -i "s#<channel>0000000000</channel>#<channel>!channelId!</channel>#" %finishXmlPath%\mmiap.xml

dos2unix %globalPath%
dos2unix %finishXmlPath%\mmiap.xml

break
	)
		)
   
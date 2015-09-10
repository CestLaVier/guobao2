call make-host-and-hfr-all.bat
call make-host-unipay-hfr-all.bat
call make-host-ctcc-hfr-all.bat
call make-host-mmsms-hfr-all.bat
pushd ..\..
_tools\notifu /t info /d 10000 /p 温馨提示 /m 生成最新android包完成，请到_packages下查看apk包
popd

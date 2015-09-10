pushd ..
call make-host.bat 4net 4399 0 true
_tools\notifu /t info /d 10000 /p 温馨提示 /m 生成最新android包完成，请到_packages下查看apk包
popd
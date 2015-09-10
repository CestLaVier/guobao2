#!/bin/bash

#================================================================#
# Copyright (c) 2010-2011 Zipline Games, Inc.
# All Rights Reserved.
# http://getmoai.com
#================================================================#

#----------------------------------------------------------------#
# application and project names
#----------------------------------------------------------------#

	project_name="guobao"
	app_name="果宝特攻"
	pkg_name="guobao"

#----------------------------------------------------------------#
# talkingdata
#----------------------------------------------------------------#	
	talkingdata_appid="403A348F5DB558816ADB26509C3CFE6A"
	talkingdata_channelid=$channel_name
#----------------------------------------------------------------#
# version numbers
#----------------------------------------------------------------#

	version_code="13"
	version_name="1.9"

#----------------------------------------------------------------#
	
	DataEye_DC_APPID="9E26D765D4C16FF070237FB3BD8E259E"
	DataEye_DC_CHANNEL=$channel_name

	if [ x"$channel_name" == xuc ]; then
	    pkg_name=$pkg_name.uc
		jpush_appkey="2c28eb85dba33a81af22579f"
	fi
	
	if [ x"$channel_name" == xnd91 ]; then
	    pkg_name=$pkg_name"91"
		jpush_appkey="7ba9c9303a5f0b69af8bc7de"
	fi
	
	if [ x"$channel_name" == xqihoo360 ]; then
	    pkg_name=$pkg_name.qihoo360
		jpush_appkey="03ec23848fcef330b177b31a"
	fi
	
	if [ x"$channel_name" == xdownjoy ]; then
	    pkg_name=$pkg_name.downjoy
		jpush_appkey="d65f17654d64a414ba23a4e0"
	fi
	
	if [ x"$channel_name" == xxiaomi ]; then
	    pkg_name=$pkg_name.mi
		jpush_appkey="6a3a0e1d68cf55fb292f4883"
	fi
	
	if [ x"$channel_name" == xduoku ]; then
	    pkg_name=$pkg_name"DK"
		jpush_appkey="4c7dccab27167fac27edfb7f"
	fi
	
	if [ x"$channel_name" == xcmgc ]; then
	    pkg_name=$pkg_name.cmgc
	fi
	
	if [ x"$channel_lua_name" == xhfr ]; then
	   ## app_name="果宝特攻:机甲大合体"
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_name" == x4net_xiaomi ]; then
	    pkg_name=$pkg_name.wali
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_name" == xoppo ]; then
	    pkg_name=$pkg_name.oppo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xoppo_hai ]; then
	    pkg_name=$pkg_name.oppo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xoppo_mm6 ]; then
	    pkg_name=$pkg_name.oppo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xoppo_mm7 ]; then
	    pkg_name=$pkg_name.oppo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xoppo_ck ]; then
	    pkg_name=$pkg_name.oppo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xoppo ]; then
	    pkg_name=$pkg_name.oppo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xbaidu ]; then
	    pkg_name=$pkg_name.baidu
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xanzhi ]; then
	    pkg_name=$pkg_name.anzhi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xyouku ]; then
	    pkg_name=$pkg_name.youku
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xbf ]; then
	    pkg_name=$pkg_name.bf
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xkuwo ]; then
	    pkg_name=$pkg_name.kuwo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xyyh ]; then
	    pkg_name=$pkg_name.yyh
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xmeitu ]; then
	    pkg_name=$pkg_name.meitu
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xlenovo ]; then
	    pkg_name=$pkg_name.lenovo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xsina ]; then
	    pkg_name=$pkg_name.sina
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xuc ]; then
	    pkg_name=$pkg_name.uc
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xiqiyi ]; then
	    pkg_name=$pkg_name.iqiyi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xpp ]; then
	    pkg_name=$pkg_name.pp
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_name" == xctcc ]; then
	    pkg_name=$pkg_name.none
	    # app_name="果宝特攻：保卫彩莲"
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_name" == xunipay ]; then
	    app_name="果宝特攻:保卫彩莲"
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	############4netbaidu
	if [ x"$channel_lua_name" == xbaidu_hai ]; then
	    pkg_name=$pkg_name.baidu
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xbaidu_ck ]; then
	    pkg_name=$pkg_name.baidu
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xbaidu_mm6 ]; then
	    pkg_name=$pkg_name.baidu
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netbaidu

	############4netanzhi
	if [ x"$channel_name" == x4net_anzhi ]; then
	    pkg_name=$pkg_name.anzhi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xanzhi_hai ]; then
	    pkg_name=$pkg_name.anzhi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xanzhi_ck ]; then
	    pkg_name=$pkg_name.anzhi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xanzhi_mm6 ]; then
	    pkg_name=$pkg_name.anzhi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xanzhi_mm7 ]; then
	    pkg_name=$pkg_name.anzhi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netanzhi

	############4netkuwo
	if [ x"$channel_lua_name" == xkuwo_hai ]; then
	    pkg_name=$pkg_name.kuwo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xkuwo_ck ]; then
	    pkg_name=$pkg_name.kuwo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xkuwo_mm6 ]; then
	    pkg_name=$pkg_name.kuwo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xkuwo_mm7 ]; then
	    pkg_name=$pkg_name.kuwo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netkuwo

	############4netlenovo
	if [ x"$channel_lua_name" == xlenovo_hai ]; then
	    pkg_name=$pkg_name.lenovo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xlenovo_ck ]; then
	    pkg_name=$pkg_name.lenovo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xlenovo_mm6 ]; then
	    pkg_name=$pkg_name.lenovo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xlenovo_mm7 ]; then
	    pkg_name=$pkg_name.lenovo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netlenovo

	############4netpp
	if [ x"$channel_lua_name" == xpp_hai ]; then
	    pkg_name=$pkg_name.pp
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xpp_ck ]; then
	    pkg_name=$pkg_name.pp
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xpp_mm6 ]; then
	    pkg_name=$pkg_name.pp
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xpp_mm7 ]; then
	    pkg_name=$pkg_name.pp
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netpp

	############4netvivo
	if [ x"$channel_lua_name" == xvivo_hai ]; then
	    pkg_name=$pkg_name.vivo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xvivo_ck ]; then
	    pkg_name=$pkg_name.vivo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xvivo_mm6 ]; then
	    pkg_name=$pkg_name.vivo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xvivo_mm7 ]; then
	    pkg_name=$pkg_name.vivo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xvivo ]; then
	    pkg_name=$pkg_name.vivo
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netvivo

	############4netyyh
	if [ x"$channel_lua_name" == xyyh_hai ]; then
	    pkg_name=$pkg_name.yyh
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xyyh_ck ]; then
	    pkg_name=$pkg_name.yyh
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xyyh_mm6 ]; then
	    pkg_name=$pkg_name.yyh
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xyyh_mm7 ]; then
	    pkg_name=$pkg_name.yyh
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netyyh

	############4netiqiyi
	if [ x"$channel_lua_name" == xiqiyi_hai ]; then
	    pkg_name=$pkg_name.iqiyi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xiqiyi_ck ]; then
	    pkg_name=$pkg_name.iqiyi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xiqiyi_mm6 ]; then
	    pkg_name=$pkg_name.iqiyi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xiqiyi_mm7 ]; then
	    pkg_name=$pkg_name.iqiyi
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netiqiyi

	############4netbaidu
	if [ x"$channel_name" == x4net_baidu ]; then
	    pkg_name=$pkg_name.baidu
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netbaidu
	############4netyouku
	if [ x"$channel_lua_name" == xyouku_hai ]; then
	    pkg_name=$pkg_name.youku
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xyouku_ck ]; then
	    pkg_name=$pkg_name.youku
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xyouku_mm6 ]; then
	    pkg_name=$pkg_name.youku
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	
	if [ x"$channel_lua_name" == xyouku_mm7 ]; then
	    pkg_name=$pkg_name.youku
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netyouku

	############4netuc
	if [ x"$channel_name" == x4net_uc ]; then
	    pkg_name=$pkg_name.uc
	    # app_name="果宝特攻：保卫彩莲"
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netuc

	############4netyouku
	if [ x"$channel_lua_name" == xbf_hai ]; then
	    pkg_name=$pkg_name.bf
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xbf_ck ]; then
	    pkg_name=$pkg_name.bf
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xbf_mm6 ]; then
	    pkg_name=$pkg_name.bf
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xbf_mm7 ]; then
	    pkg_name=$pkg_name.bf
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	############4netyouk

	if [ x"$channel_lua_name" == xsina_hai ]; then
	    pkg_name=$pkg_name.sina
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xsina_ck ]; then
	    pkg_name=$pkg_name.sina
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi

	if [ x"$channel_lua_name" == xsina_mm6 ]; then
	    pkg_name=$pkg_name.sina
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi
	if [ x"$channel_lua_name" == xsina_mm7 ]; then
	    pkg_name=$pkg_name.sina
		jpush_appkey="1aa92636ba5f53f8357f9cf4"
	fi




	# if [ x"$channel_name" == xallsms ]; then
	#     pkg_name=$pkg_name.$channel_lua_name
	# fi

# space-delimited list of libraries (moai-supported) required 
# (this list is created by make-host.sh using command-line 
# information)
# available: facebook, tapjoy, crittercism, google-push, 
#            google-billing, miscellaneous (required by google-billing)
#----------------------------------------------------------------#
# domob sharesdk
	# require_module="fmod dataeye domob ulcdkey"

	if [ x"$channel_name" != x ]; then
	    require_module="$require_module $channel_name"
	fi

	requires=( $require_module )

	
#----------------------------------------------------------------#
# list of icon files
#----------------------------------------------------------------#

	icon_ldpi="res/icon-ldpi.png"
	icon_mdpi="res/icon-mdpi.png"
	icon_hdpi="res/icon-hdpi.png"
	icon_xhdpi="res/icon-xhdpi.png"

	if [ x"$channel_name" != x ]; then
	    icon_ldpi="res/icon-ldpi-"$channel_name.png
	    icon_mdpi="res/icon-mdpi-"$channel_name.png
	    icon_hdpi="res/icon-hdpi-"$channel_name.png
	    icon_xhdpi="res/icon-xhdpi-"$channel_name.png
	fi
		
#----------------------------------------------------------------#
# working directory in the assets directory of the application 
# bundle and a space-delimited list of lua files thereunder to run 
# when the application starts
#----------------------------------------------------------------#

	working_dir="lua"
	run=( "main.lua" )
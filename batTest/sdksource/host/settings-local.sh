#!/bin/bash

#================================================================#
# Copyright (c) 2010-2011 Zipline Games, Inc.
# All Rights Reserved.
# http://getmoai.com
#================================================================#

#----------------------------------------------------------------#
# path to Android SDK folder (on Windows, you MUST use forward 
# slashes as directory separators, e.g. C:/android-sdk)
#----------------------------------------------------------------#

	android_sdk_root="E:/Developsoft/adt-bundle-windows-x86_64-20140702/sdk"

#----------------------------------------------------------------#
# space-delimited list of source lua directories to add to the 
# application bundle and corresponding destination directories in 
# the assets directory of the bundle
#----------------------------------------------------------------#
	loader_dir_common="../make/common"
	loader_dir_patches="../make/dpatches"
	loader_dir_channel="../make/channelsrc"
	if [ x"$channel_lua_name" != x ]; then
	   channel_dir="$loader_dir_channel/"$channel_lua_name
	fi
	loader_src="$loader_dir_common $channel_dir"
	loader_dest="lua lua"
	if [ x"$use_all_in_one" == xtrue ]; then
		loader_src="$loader_src $loader_dir_patches"
		loader_dest="$loader_dest lua/dpatches"
	fi
	#src_dirs=( $loader_src )
	#src_dirs=( "../make_lua/bin")
	src_dirs=( "../../5_release/client/src")
	dest_dirs=( $loader_dest )
#----------------------------------------------------------------#
# Set the orientation of the screen.(landscape, portrait, etc...)
#----------------------------------------------------------------#

	screenOrientation="sensorLandscape"

#----------------------------------------------------------------#
# Set the orientation of the screen.(landscape, portrait, etc...)
#----------------------------------------------------------------#

	enable_accelerometer="false"	
#----------------------------------------------------------------#
# debug & release settings
# you must define key store data in order to build for release
#----------------------------------------------------------------#

	debug=true
	key_store=
	key_alias=
	key_store_password=
	key_alias_password=
	
	if [ x"$channel_name" != x ]; then
		debug=false
		key_store=key/"$channel_name"_key
		key_alias="$channel_name"_"$channel_name"
		
		key_store_password="$channel_name"_storepass
		key_alias_password="$channel_name"_keypass
			if [ x"$channel_name" == x"downjoy" ]; then
			key_alias="864"
			key_store_password="downjoy_864"
			key_alias_password="downjoy_864"
			fi
	fi
	
MOAI14_SDK=E:/ude_guobao2/gb2/batTest/gameProjetc/sdk

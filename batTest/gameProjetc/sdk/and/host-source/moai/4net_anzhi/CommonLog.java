//----------------------------------------------------------------//
// Copyright (c) 2010-2011 Zipline Games, Inc. 
// All Rights Reserved. 
// http://getmoai.com
//----------------------------------------------------------------//

package com.ziplinegames.moai;

import android.util.Log;

public class CommonLog {

	public static boolean isLog = false;
	//----------------------------------------------------------------//
	public static void e ( String message, Exception e ) {
		
		if (!isLog){ return;}
	    Log.e ( "CommonLog", message, e );
	    
	}

	//----------------------------------------------------------------//
	public static void e ( String Tag, String  message ) {
		
		if (!isLog){ return;}
	    Log.e ( Tag, message );
	    
	}
	
	//----------------------------------------------------------------//
	public static void e ( String  message ) {
		
		if (!isLog){ return;}
	    Log.e ( "CommonTool",  message );
	    
	}
	
	//----------------------------------------------------------------//
	public static void w ( String Tag, String  message ) {
		
		if (!isLog){ return;}
	    Log.w ( Tag, message );
	   
	}

	//----------------------------------------------------------------//
	public static void d ( String Tag, String  message ) {
		
		if (!isLog){ return;}
	    Log.d ( Tag, message );
	}
	
	//----------------------------------------------------------------//
	public static void d ( String  message ) {
		
		if (!isLog){ return;}
	    Log.d ( "CommonTool", message );
	    
	}

	//----------------------------------------------------------------//
	public static void i ( String Tag, String  message ) {
		
		if (!isLog){ return;}
	    Log.i ( Tag, message );
	    
	}
	
	//----------------------------------------------------------------//
	public static void i (String  message ) {
		
		if (!isLog){ return;}
	    Log.i ( "CommonTool", message );
	    
	}


}
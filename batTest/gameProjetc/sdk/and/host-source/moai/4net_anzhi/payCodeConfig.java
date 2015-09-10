package com.ziplinegames.moai;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class payCodeConfig {
	public static List<payCodeConfig> pCodeList=null;
	public String payCode; 
	public int money;
	public String number;
	public String name;
	 public static void setPayCodeConfig(){
	        if(pCodeList!=null||CommonBaseSdk.sConfigJsonObject==null) return;
	        
	        //读取计费点
	        JsonValue jsonVal=CommonBaseSdk.sConfigJsonObject.get("payCodes");
	        if(jsonVal==null) return;
	        JsonArray billingMap=jsonVal.asArray();
	        if(billingMap!=null){
	            pCodeList=new ArrayList<payCodeConfig>();         
	            JsonObject mapJson=null;
	            for(int i=0;i<billingMap.size();i++){
	            	payCodeConfig bConfig=new payCodeConfig();
	                mapJson=billingMap.get(i).asObject(); 
	                bConfig.payCode=CommonBaseSdk.GetJsonVal(mapJson,"payCode","000");
	                bConfig.money=CommonBaseSdk.GetJsonValInt(mapJson,"money",0);
	                bConfig.number=CommonBaseSdk.GetJsonVal(mapJson,"number","0");
	                
	                String nametring = null;
	                String nameUTF8 = null;
					try {
						nametring = new String(CommonBaseSdk.GetJsonVal(mapJson,"name","0").getBytes("UTF-8"));
						nameUTF8 = URLEncoder.encode(nametring, "UTF-8"); 
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
	                
	                
	                bConfig.name=nametring;
	                pCodeList.add(bConfig);
	            }
	            
	            
	        }
	    }
	 ///
	 public static payCodeConfig getPayCodeConfig(payCodeConfig bconfig){
	        if(bconfig==null) return bconfig;
	        if(pCodeList==null||CommonBaseSdk.sConfigJsonObject==null) return null;     
	        if(pCodeList.size()<=0) return null;
	        payCodeConfig _bconfig;    
	        for(int i=0;i<pCodeList.size();i++){
	            _bconfig=pCodeList.get(i);
	            if(bconfig.number!="0"){
	                if(_bconfig.number.equalsIgnoreCase(bconfig.number)) return _bconfig;
	            }
	            else
	            {
	                if(_bconfig.money==bconfig.money) return _bconfig;
	            }
	        }
	        return null;
	    }
	    
}

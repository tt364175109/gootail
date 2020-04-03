package com.oep.live;

public class StringUtils {
	public static String arrayToString(long[] array){
		String rtnStr = "";
		boolean flag = true;
		for(long e:array){
			if(flag){
				rtnStr = e+"";
				flag = false;
			}else{
				rtnStr = rtnStr + "," + e;
			}
		}
		return rtnStr;
	}
}

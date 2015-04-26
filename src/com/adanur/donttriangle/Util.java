package com.adanur.donttriangle;

import java.util.Random;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class Util {

	public static String UserName;
	
	public static String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        UserName = sb.toString().substring(0, numchars);
        return UserName;
    }

}

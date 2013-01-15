package net.thinkbase.js.rhino.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Utility methods
 * @author thinkbase.net
 */
public class Utils {
	public static final String buildUUID(){
		UUID uuid  =  UUID.randomUUID();
		return uuid.toString();
	}

	public static String MD5(String source) {
	   try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(source.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException e) {
	    	throw new RuntimeException(e);
	    }
	}

    public static final String readAsText(InputStream input, String charset) throws IOException{
        Reader rd;
        if (null==charset){
            rd = new InputStreamReader(input);
        }else{
            rd = new InputStreamReader(input, charset);
        }
        StringBuffer buf = new StringBuffer();
        int c = rd.read();
        while(-1!=c){
            buf.append((char)c);
            c = rd.read();
        }
        rd.close();
        return buf.toString();
    }
    public static final String readAsText(InputStream input) throws IOException{
        return readAsText(input, "UTF-8");
    }
}

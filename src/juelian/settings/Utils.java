package juelian.settings;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.util.ArrayList;

import android.R.integer;
import android.os.SystemProperties;

public class Utils {
	
	//public static String winer = ;
	public static boolean isMe = SystemProperties.get("ro.product.mod_device").equalsIgnoreCase("s291_juelian");

    public static void runCmd(String cmd){
    	try {
    		boolean root = true;
    		if (root) {
    			Process p = Runtime.getRuntime().exec("su");
    			PrintWriter pw = new PrintWriter(p.getOutputStream());
    			pw.println(cmd);
    			pw.flush();
    			pw.close();
    			p.waitFor();
    			//return true;
    		} else {
	    		Process p = Runtime.getRuntime().exec(cmd);
	    		p.waitFor();
	    		//return true;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return false;
    }
    
	/**
	 * @param what 0--> normal, 1-->shutdown 2 -->recovery, 3 --> bootloader
	 */
	public static void rebootForWhat(int what){
		switch (what) {
		case 0:
			Utils.runCmd("reboot");
			break;
		case 1:
			Utils.runCmd("reboot -p");
			break;
		case 2:
			Utils.runCmd("reboot recovery");
			break;
		case 3:
			Utils.runCmd("reboot bootloader");
			break;
		case 4:
			Utils.runCmd("busybox killall system_server");
			break;
		}
	}
}

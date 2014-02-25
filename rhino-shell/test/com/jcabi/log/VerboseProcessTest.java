package com.jcabi.log;

import java.io.File;
import java.sql.Timestamp;

import org.junit.Test;

public class VerboseProcessTest {
	public static void main(String[] args) throws Exception {
		new VerboseProcessTest().test();
	}

	@Test
	public void test() throws Exception {
		String[] cmd;
		ProcessBuilder pb;
		StringBuffer[] out;
		
		String os = System.getProperty("os.name");
		//First test - ping
		System.err.println("================================================================================");
		if (os.startsWith("Windows")){
			cmd = new String[]{"ping", "localhost", "-n", "10"};
		}else{
			cmd = new String[]{"ping", "localhost", "-c", "10"};
		}
		pb = new ProcessBuilder(cmd);
		out = new VerboseProcess(pb).stdoutQuietly();
		System.out.println("[STDOUT]" + out[0]);
		System.err.println("[STDERR]" + out[1]);
		//Second test - shell script
		System.err.println("================================================================================");
		if (os.startsWith("Windows")){
			File bat = new File(VerboseProcessTest.class.getResource("test.bat").toURI());
			cmd = new String[]{bat.getCanonicalPath(), "p1", "p2", "p3"};
			pb = new ProcessBuilder(cmd);
		}else{
			File sh = new File(VerboseProcessTest.class.getResource("test.sh").toURI());
			cmd = new String[]{"/bin/bash", "-c", ". "+sh.getCanonicalPath()+" \"p1\" \"p2\" \"p3\""};
			pb = new ProcessBuilder(cmd);
		}
		pb.environment().put("NOW", new Timestamp(System.currentTimeMillis()).toString());
		out = new VerboseProcess(pb).stdoutQuietly();
		System.out.println("[STDOUT]" + out[0]);
		System.err.println("[STDERR]" + out[1]);
		//Third test - stderr
		System.err.println("================================================================================");
		if (os.startsWith("Windows")){
			cmd = new String[]{"cmd", "/c", "dir /*"};
		}else{
			cmd = new String[]{"/bin/bash", "-c", "ls -*"};
		}
		pb = new ProcessBuilder(cmd);
		out = new VerboseProcess(pb).stdoutQuietly();
		System.out.println("[STDOUT]" + out[0]);
		System.err.println("[STDERR]" + out[1]);
	}

}

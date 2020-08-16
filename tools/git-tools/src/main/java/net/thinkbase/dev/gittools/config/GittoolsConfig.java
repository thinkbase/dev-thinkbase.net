package net.thinkbase.dev.gittools.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import net.thinkbase.dev.gittools.GittoolsApplication;

@Component
public class GittoolsConfig {
	public static final String EXPORT_WORK_DIR = "ExportWorkDir";

	@Bean(name=EXPORT_WORK_DIR)
	public File getExportWorkDir() throws IOException {
		String folder = GittoolsApplication.class.getName();
		File tmp = Files.createTempDirectory(null).toFile().getParentFile();
		File dir = new File(tmp, folder);
		dir.mkdirs();
		return dir;
	}

}

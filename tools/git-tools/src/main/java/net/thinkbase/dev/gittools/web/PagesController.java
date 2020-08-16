package net.thinkbase.dev.gittools.web;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.thinkbase.dev.gittools.config.GittoolsConfig;
import reactor.core.publisher.Mono;

@Controller
public class PagesController {
	@Autowired
	@Qualifier(GittoolsConfig.EXPORT_WORK_DIR)
	private File exportWorkDir;

	@RequestMapping("/")
    public String index(Model model){
		model.addAttribute("version", System.getProperty("java.vm.name"));
        return "index";
    }

	@RequestMapping("/file/{fileName}")
    public Mono<Void> download(@PathVariable("fileName") String fileName, ServerHttpResponse response) throws IOException{
		File file = new File(exportWorkDir, fileName);
		
		ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
		
        response.getHeaders().set(
        		HttpHeaders.CONTENT_DISPOSITION,
        		"attachment;filename=" + URLEncoder.encode(fileName, CharEncoding.UTF_8).replace("+", "%20"));
        
        MediaType mt;
        if (fileName.endsWith("xlsx")) {
        	mt = new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }else {
        	mt = new MediaType("application", "octet-stream");
        }
        response.getHeaders().setContentType(mt);

        return zeroCopyResponse.writeWith(file, 0, file.length());
    }

}

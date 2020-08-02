package net.thinkbase.dev.gittools.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PagesController {
	@RequestMapping("/")
    public String index(Model model){
		model.addAttribute("version", System.getProperty("java.vm.name"));
        return "index";
    }
}

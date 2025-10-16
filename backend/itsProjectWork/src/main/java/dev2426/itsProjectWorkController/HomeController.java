package dev2426.itsProjectWorkController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
	public class HomeController {

		@GetMapping("/home")
		public String mostraPagina() {
			return "homepage.html";
		}

	

}

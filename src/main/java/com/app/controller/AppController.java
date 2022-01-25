package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.model.LocationStates;
import com.app.service.CoronaVirusDataService;

@Controller
public class AppController {

	@Autowired
	CoronaVirusDataService coronaVirusDataService;

	@GetMapping("/")
	public String home(Model model) {
		List<LocationStates> allStates = coronaVirusDataService.getAllStates();
		int totalReportedcase = allStates.stream().mapToInt(state -> state.getLastTotalCase()).sum();
		model.addAttribute("totalReportedcase", totalReportedcase);
		model.addAttribute("coronaData", allStates);
	

		return "home";
	}
}

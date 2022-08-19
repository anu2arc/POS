package com.increff.pos.controller;

import com.increff.pos.model.Data.InfoData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

	@Value("${app.baseUrl}")
	private String baseUrl;

	@RequestMapping(value = "")
	public String index() {
		return "index.html";
	}

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/placeorder")
	public ModelAndView placeOrder() {
		return mav("placeorder.html");
	}

	@RequestMapping(value = "/ui/vieworder")
	public ModelAndView viewOrder() {
		return mav("vieworder.html");
	}

	@RequestMapping(value = "/ui/reports")
	public ModelAndView reports() {
		return mav("reports.html");
	}

	private ModelAndView mav(String page) {
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("info", new InfoData());
		mav.addObject("baseUrl", baseUrl);
		return mav;
	}

}

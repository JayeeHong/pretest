package com.jayee.pretest.controller;

import com.jayee.pretest.repository.KakaoSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {
    @RequestMapping("/")
    public String home(Model model) {
        log.info("home Controller Start::::::");

        model.addAttribute("searchParam", new KakaoSearch());

        return "home";
    }
}

package com.workable.matchmakers.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Home redirection to swagger api documentation
 */
@Controller
@RequestMapping(value = "/api")
public class HomeController extends BaseMatchmakersController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        // return "redirect:/docs/api.html";
        return "redirect:/swagger-ui.html";
    }
}

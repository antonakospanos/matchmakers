package com.workable.matchmakers.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.workable.matchmakers.web.api.MatchmakersBaseController.API_BASE;

/**
 * Home redirection to swagger api documentation
 */
@Controller
@RequestMapping(value = API_BASE)
public class HomeController extends MatchmakersBaseController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        // return "redirect:/docs/api.html";
        return "redirect:/swagger-ui.html";
    }
}

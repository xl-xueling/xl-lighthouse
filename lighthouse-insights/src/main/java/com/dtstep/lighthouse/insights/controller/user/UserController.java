package com.dtstep.lighthouse.insights.controller.user;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@ControllerAdvice
public class UserController {

    @RequestMapping("/index.shtml")
    public ModelAndView index(ModelMap model) throws Exception {
        return null;
    }
}

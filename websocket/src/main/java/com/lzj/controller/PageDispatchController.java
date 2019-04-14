package com.lzj.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Scope("prototype")
@Controller
@RequestMapping("/dist")
public class PageDispatchController {

    @RequestMapping("/index")
    public ModelAndView goIndex(){
        System.out.println("/index");
        ModelAndView mv = new ModelAndView();

        mv.setViewName("/dist/index.html");

        return mv;
    }
}

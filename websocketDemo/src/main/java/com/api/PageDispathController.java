package com.api;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 数据实体管理
 * 
 * @author zhengliexin 2017年2月15日
 * @version $Revision: 1.0
 */
@Scope("prototype")
@Controller
@RequestMapping(value = "/dist")
public class PageDispathController {


	@RequestMapping(value = "/index",method = { RequestMethod.GET, RequestMethod.POST ,RequestMethod.HEAD})//
	public ModelAndView goIndex() {
		
		System.out.println("/index");
		ModelAndView mv = new ModelAndView();
		
//		mv.setViewName("/WEB-INF/views/dist/index.html");
		mv.setViewName("/dist/index.html");
//		mv.setViewName("/dist/index.html"+"?verify_code="+verify_code);
//		mv.setViewName("redirect:/dist/index.html"+"?verify_code="+verify_code);
//		 attr.addAttribute("verify_code", verify_code);  
//         attr.addFlashAttribute("verify_code",verify_code);
//		mv.setViewName("/dist/index.html"+"?verify_code="+verify_code);

		return mv;
	}

}

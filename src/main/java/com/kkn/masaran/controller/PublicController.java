package com.kkn.masaran.controller;

import com.kkn.masaran.model.User;
import com.kkn.masaran.service.service.PublicService;
import com.kkn.masaran.utility.Helper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ARDI on 6/22/2017.
 */

@Controller
public class PublicController {

    @Autowired
    PublicService publicService;
    @Autowired
    private Helper helper;

    @ModelAttribute("helper")
    public Helper getHelper() {
        return helper;
    }

    @ModelAttribute("activeUser")
    public User getActiveUser() {
        return helper.getCurrentUser();
    }

    @RequestMapping(value="/" , method= RequestMethod.GET)
    public String landingPage(
        HttpServletRequest request,
        Model model)
    {
        return publicService.landingPageGetService(request, model);
    }

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String authenticateUser(
        @RequestParam(value = "error" , required = false) String error,
        @RequestParam(value = "logout" , required = false) String logout,
        @ModelAttribute User newUser,
        HttpServletRequest request,
        Model model)
    {
        return publicService.authenticateUserService(error, logout, newUser, request, model);
    }

    @RequestMapping(value="/login/process")
    public String decideUser(Model model) {
        return publicService.decideUserService(model);
    }

    @RequestMapping(value = "/bahan-baku",method = RequestMethod.GET)
    public String showAllRawMaterial(Model model, HttpServletRequest request){
        return publicService.showAllRawMaterialService(model, request);
    }

    @RequestMapping(value = "/catalog",method = RequestMethod.GET)
    public String showAllProduct(Model model, HttpServletRequest request){
        return publicService.showAllProductService(model, request);
    }
}

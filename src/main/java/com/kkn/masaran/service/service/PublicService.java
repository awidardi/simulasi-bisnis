package com.kkn.masaran.service.service;

import com.kkn.masaran.model.User;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ARDI on 6/29/2017.
 */

@Service
public interface PublicService {

    public String authenticateUserService(
        @RequestParam(value = "error" , required = false) String error,
        @RequestParam(value = "logout" , required = false) String logout,
        @ModelAttribute User newUser,
        HttpServletRequest request,
        Model model);

    public String decideUserService(Model model);

    public String showAllRawMaterialService(Model model, HttpServletRequest request);

    public String landingPageGetService(HttpServletRequest request, Model model);

    public String showAllProductService(
        Model model,
        HttpServletRequest request);

}

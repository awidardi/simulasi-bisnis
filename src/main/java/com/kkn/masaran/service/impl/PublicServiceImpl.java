package com.kkn.masaran.service.impl;

import com.kkn.masaran.model.*;
import com.kkn.masaran.repository.BusinessmanRepository;
import com.kkn.masaran.repository.ProductRepository;
import com.kkn.masaran.repository.RawMaterialRepository;
import com.kkn.masaran.service.service.PublicService;
import com.kkn.masaran.utility.Helper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private Helper helper;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessmanRepository businessmanRepository;

    @ModelAttribute("helper")
    public Helper getHelper() {
        return helper;
    }

    @ModelAttribute("activeUser")
    public User getActiveUser() {
        return helper.getCurrentUser();
    }

    @Override
    public String authenticateUserService(
        @RequestParam(value = "error" , required = false) String error,
        @RequestParam(value = "logout" , required = false) String logout,
        @ModelAttribute User newUser,
        HttpServletRequest request,
        Model model)
    {
        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");

            System.out.print("Haha");
            //login form for update page
            //if login error, get the targetUrl from session again.
            String targetUrl = helper.getRememberMeTargetUrlFromSession(request);
            System.out.println(targetUrl);
            if(StringUtils.hasText(targetUrl)){
                model.addAttribute("targetUrl", targetUrl);
            }

        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }

        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        return "public/login";
    }

    @Override
    public String decideUserService(Model model) {
        User user = helper.getCurrentUser();

        if(user instanceof Juragan){
            return "redirect:/my-juragan/profile";
        }else if(user instanceof Businessman){
            return "redirect:/my-umkm/profile";
        }
        return "redirect:/";
    }

    @Override
    public String showAllRawMaterialService(
        Model model,
        HttpServletRequest request)
    {
        final String PAGE_PARAMETER = "page";
        final String SIZE_PARAMETER = "count";
        final String ORDER_BY_PARAMETER = "sort";
        int currentPage;
        int rawMaterialPerPage;
        String sortBy = "id";

        // Try to get current page from URL, if parameter is empty, open first page
        try {
            currentPage = Integer.parseInt(request.getParameter(PAGE_PARAMETER));
        }
        catch (NumberFormatException e) {
            currentPage = 1;
        }
        try {
            rawMaterialPerPage = Integer.parseInt(request.getParameter(SIZE_PARAMETER));
        }
        catch (NumberFormatException e) {
            rawMaterialPerPage = 8;
        }
        if (request.getParameter(ORDER_BY_PARAMETER) != null) {
            sortBy = request.getParameter(ORDER_BY_PARAMETER);
        }

        // variables related to pagination calculation
        int startIndex = (currentPage-1) * rawMaterialPerPage;
        int endIndex = startIndex + rawMaterialPerPage;
        int rawMaterialLastIndex;
        List<RawMaterial> rawMaterialList;

        // get catering in order of what our request ask.
        System.out.println(sortBy);
        if (sortBy.equals("name")) {
            rawMaterialList = rawMaterialRepository.findAllByOrderByName();
        } else {
            rawMaterialList = rawMaterialRepository.findByStockExist(true);
        }
        rawMaterialLastIndex = rawMaterialList.size() - 1;

        // Please don't try to process negative page :)
        if (currentPage < 1) {
            return "redirect:/bahan-baku";
        }

        // Avoid accessing out of bound page
        if (startIndex > rawMaterialLastIndex) {
            return "redirect:/bahan-baku?" + PAGE_PARAMETER + "=" + (currentPage-1) + "&"+ SIZE_PARAMETER + "=" + rawMaterialPerPage;
        }

        // End of list cutting  must match Catering number
        if (endIndex > rawMaterialLastIndex) {
            endIndex = rawMaterialLastIndex+1;
            model.addAttribute("isLastPage", true);
        }

        if (currentPage == 1) {
            model.addAttribute("isFirstPage", true);
        }
        List<RawMaterial> subset = rawMaterialList.subList(startIndex, endIndex);

        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        model.addAttribute("rawMaterials", subset);
        model.addAttribute("rawMaterialPerPage", rawMaterialPerPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("prevPage", currentPage-1);
        model.addAttribute("nextPage", currentPage+1);
        model.addAttribute("start", startIndex+1);
        model.addAttribute("end", endIndex);
        model.addAttribute("total", rawMaterialLastIndex+1);

        return "public/bahan-baku";
    }

    @Override
    public String landingPageGetService(
        HttpServletRequest request,
        Model model)
    {
        List<RawMaterial> rawMaterialList = rawMaterialRepository.findByIsDiscountedOrderByName(true);
        List<Businessman> businessmanList = businessmanRepository.findAll();
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        model.addAttribute("rawMaterialList", rawMaterialList);
        model.addAttribute("businessmans", businessmanList);
        return "public/landing";
    }

    @Override
    public String showAllProductService(
        Model model,
        HttpServletRequest request)
    {
        final String PAGE_PARAMETER = "page";
        final String SIZE_PARAMETER = "count";
        final String ORDER_BY_PARAMETER = "sort";
        int currentPage;
        int productPerPage;
        String sortBy = "id";

        // Try to get current page from URL, if parameter is empty, open first page
        try {
            currentPage = Integer.parseInt(request.getParameter(PAGE_PARAMETER));
        }
        catch (NumberFormatException e) {
            currentPage = 1;
        }
        try {
            productPerPage = Integer.parseInt(request.getParameter(SIZE_PARAMETER));
        }
        catch (NumberFormatException e) {
            productPerPage = 8;
        }
        if (request.getParameter(ORDER_BY_PARAMETER) != null) {
            sortBy = request.getParameter(ORDER_BY_PARAMETER);
        }

        // variables related to pagination calculation
        int startIndex = (currentPage-1) * productPerPage;
        int endIndex = startIndex + productPerPage;
        int productLastIndex;
        List<Product> productList;

        // get catering in order of what our request ask.
        System.out.println(sortBy);
        if (sortBy.equals("name")) {
            productList = productRepository.findByIsSoldOrderByName(true);
            //productList = productRepository.findAll();
        } else {
            productList = productRepository.findByIsSold(true);
            //productList = productRepository.findAll();
        }
        productLastIndex = productList.size() - 1;

        // Please don't try to process negative page :)
        if (currentPage < 1) {
            return "redirect:/catalog";
        }

        // Avoid accessing out of bound page
        if (startIndex > productLastIndex) {
            return "redirect:/catalog?" + PAGE_PARAMETER + "=" + (currentPage-1) + "&"+ SIZE_PARAMETER + "=" + productPerPage;
        }

        // End of list cutting  must match Catering number
        if (endIndex > productLastIndex) {
            endIndex = productLastIndex+1;
            model.addAttribute("isLastPage", true);
        }

        if (currentPage == 1) {
            model.addAttribute("isFirstPage", true);
        }
        List<Product> subset = productList.subList(startIndex, endIndex);

        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        model.addAttribute("rawMaterials", subset);
        model.addAttribute("rawMaterialPerPage", productPerPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("prevPage", currentPage-1);
        model.addAttribute("nextPage", currentPage+1);
        model.addAttribute("start", startIndex+1);
        model.addAttribute("end", endIndex);
        model.addAttribute("total", productLastIndex+1);

        return "public/catalog";
    }
}

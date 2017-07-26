package com.kkn.masaran.controller;

import com.kkn.masaran.model.Businessman;
import com.kkn.masaran.model.Product;
import com.kkn.masaran.model.User;
import com.kkn.masaran.model.UserRole;
import com.kkn.masaran.repository.BusinessmanRepository;
import com.kkn.masaran.repository.UserRoleRepository;
import com.kkn.masaran.service.service.BusinessmanService;
import com.kkn.masaran.service.service.OrderService;
import com.kkn.masaran.utility.Helper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ARDI on 6/22/2017.
 */

@Controller
public class BusinessmanController {

    @Autowired
    private BusinessmanService businessmanService;
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

    @RequestMapping(value="/umkm/daftar" , method= RequestMethod.GET)
    public String businessmanRegisterGet(
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.businessmanRegisterGetService(model, request);
    }

    @RequestMapping(value="/umkm/daftar",method=RequestMethod.POST)
    public String businessmanRegisterPost(
        @ModelAttribute Businessman businessman,
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.businessmanRegisterPostService(businessman, model, request);
    }

    @RequestMapping(value="/my-umkm/profile",method=RequestMethod.GET)
    public String umkmProfile(
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.umkmProfileService(model, request);
    }

    @RequestMapping(value = "/my-umkm/addproducts",method = RequestMethod.GET)
    public String umkmGetAddProduct(
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.umkmGetAddProductService(model, request);
    }

    @RequestMapping(value = "/my-umkm/addproducts",method = RequestMethod.POST)
    public String umkmPostAddProduct(
        @ModelAttribute Product newProduct,
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
    {
        return businessmanService.umkmPostAddProductService(newProduct, file, request);
    }

    @RequestMapping(value="/my-umkm/product/{id}/edit", method= RequestMethod.GET)
    public String editProductGet(
        @PathVariable long id,
        HttpServletRequest request,
        Model model)
    {
        return businessmanService.editProductGetService(id, request, model);
    }

    @RequestMapping(value = "/my-umkm/product/{id}/edit" , method = RequestMethod.POST)
    public String editProductPost(
        @RequestParam("file") MultipartFile file,
        @PathVariable long id,
        HttpServletRequest request)
    {
        return businessmanService.editProductPostService(file, id, request);
    }

    @RequestMapping(value = "/my-umkm/product/{id}/delete", method = RequestMethod.POST)
    public String deleteProduct(
        @PathVariable long id)
    {
        return businessmanService.deleteProductService(id);
    }

    @RequestMapping(value = "/my-umkm/product/{id}/sell", method = RequestMethod.POST)
    public String sellNewProduct(
        @PathVariable long id,
        HttpServletRequest request)
    {
        return businessmanService.sellNewProductService(id, request);
    }

    @RequestMapping(value = "/my-umkm/product/{id}/cancel-sell", method = RequestMethod.POST)
    public String sellProductCancel(
        @PathVariable long id,
        HttpServletRequest request)
    {
        return businessmanService.sellProductCancelService(id, request);
    }

    @RequestMapping(value = "/my-umkm/orderRaw", method = RequestMethod.GET)
    public String orderRawIndex(
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.orderRawIndexService(model, request);
    }

    @RequestMapping(value = "/my-umkm/orderRaw/new", method = RequestMethod.POST)
    public String createNewOrderRaw(
        HttpServletRequest request)
    {
        return businessmanService.createNewOrderRawService(request);
    }

    @RequestMapping(value = "/my-umkm/orderRaw/{id}/cart", method = RequestMethod.GET)
    public String orderRawCart(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.orderRawCartService(id, model, request);
    }

    @RequestMapping(value = "/my-umkm/orderRaw/{id}/confirm", method = RequestMethod.POST)
    public String checkoutOrderRaw(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.checkoutOrderRawService(model, request, id);
    }

    @RequestMapping(value = "/my-umkm/orderRaw/{id}/delete", method = RequestMethod.POST)
    public String deleteOrderRaw(
    Model model,
    @PathVariable int id)
    {
        return businessmanService.deleteOrderRawService(model, id);
    }

    @RequestMapping(value = "/my-umkm/order", method = RequestMethod.GET)
    public String orderIndex(
        Model model,
        HttpServletRequest request)
    {
        return businessmanService.orderIndexService(model, request);
    }

    @RequestMapping(value = "/my-umkm/order/{id}/confirm")
    public String processOrder(@PathVariable long id)
    {
        return businessmanService.processOrderService(id);
    }

    @RequestMapping(value = "/my-umkm/order/{id}/delete", method = RequestMethod.POST)
    public String deleteOrder(@PathVariable long id)
    {
        return businessmanService.deleteOrderService(id);
    }

    @RequestMapping(value = "/my-umkm/order/{id}/nego",method = RequestMethod.POST)
    public String negoOrder(
        HttpServletRequest request,
        @PathVariable long id)
    {
        return businessmanService.negoOrderService(request, id);
    }

    //editing
    @RequestMapping(value="/my-umkm/edit", method= RequestMethod.GET)
    public String editUmkmProfileGet(
        HttpServletRequest request,
        Model model)
    {
        return businessmanService.editUmkmProfileGetService(request, model);
    }

    @RequestMapping(value="/my-umkm/edit", method= RequestMethod.POST)
    public String editUmkmProfilePost(
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
    {
        return businessmanService.editUmkmProfilePostService(file, request);
    }

}

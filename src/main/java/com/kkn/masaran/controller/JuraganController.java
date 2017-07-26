package com.kkn.masaran.controller;

import com.kkn.masaran.model.Juragan;
import com.kkn.masaran.model.RawMaterial;
import com.kkn.masaran.model.User;
import com.kkn.masaran.service.service.JuraganService;
import com.kkn.masaran.utility.Helper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ARDI on 6/29/2017.
 */
@Controller
public class JuraganController {

    @Autowired
    private JuraganService juraganService;
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

    @RequestMapping(value="/juragan/daftar" , method= RequestMethod.GET)
    public String businessmanRegisterGet(
        Model model,
        HttpServletRequest request)
    {
        return juraganService.juraganRegisterGetService(model, request);
    }

    @RequestMapping(value="/juragan/daftar",method=RequestMethod.POST)
    public String businessmanRegisterPost(
        @ModelAttribute Juragan juragan,
        Model model,
        HttpServletRequest request)
    {
        return juraganService.juraganRegisterPostService(juragan, model, request);
    }

    @RequestMapping(value = "/my-juragan/profile",method = RequestMethod.GET)
    public String juraganProfile(
        Model model,
        HttpServletRequest request)
    {
        return juraganService.juraganProfileService(model, request);
    }

    @RequestMapping(value = "/my-juragan/addproducts",method = RequestMethod.GET)
    public String juraganGetAddProduct(
        Model model,
        HttpServletRequest request)
    {
        return juraganService.juraganGetAddProductService(model, request);
    }

    @RequestMapping(value = "/my-juragan/addproducts",method = RequestMethod.POST)
    public String juraganPostAddProduct(
        @ModelAttribute RawMaterial rawMaterial,
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
    {
        return juraganService.juraganPostAddProductService(rawMaterial, file, request);
    }

    @RequestMapping(value = "/my-juragan/raw-material/{id}/edit",method = RequestMethod.GET)
    public String editRawMaterialGet(
        @PathVariable long id,
        HttpServletRequest request,
        Model model)
    {
        return juraganService.editRawMaterialGetService(id, request, model);
    }

    @RequestMapping(value = "/my-juragan/raw-material/{id}/edit",method = RequestMethod.POST)
    public String editRawMaterialGet(
        @RequestParam("file") MultipartFile file,
        @PathVariable long id,
        HttpServletRequest request)
    {
        return juraganService.editRawMaterialPostService(file, id, request);
    }

    @RequestMapping(value = "/my-juragan/raw-material/{id}/delete",method = RequestMethod.POST)
    public String deleteRawMaterial(
        @PathVariable long id)
    {
        return juraganService.deleteRawMaterialService(id);
    }

    @RequestMapping(value = "/my-juragan/raw-material/{id}/add-promo",method = RequestMethod.GET)
    public String addPromoGet(
        @PathVariable long id,
        HttpServletRequest request,
        Model model)
    {
        return juraganService.addPromoGetService(id, request, model);
    }

    @RequestMapping(value = "/my-juragan/raw-material/{id}/add-promo",method = RequestMethod.POST)
    public String addPromoPost(
        HttpServletRequest request,
        @PathVariable long id)
    {
        return juraganService.addPromoPostService(request, id);
    }

    @RequestMapping(value = "/my-juragan/raw-material/{id}/stop-promo",method = RequestMethod.POST)
    public String stopPromo(
        @PathVariable long id)
    {
        return juraganService.stopPromoService(id);
    }

    @RequestMapping(value = "/my-juragan/orderRaw", method = RequestMethod.GET)
    public String orderRawIndex(Model model)
    {
        return juraganService.orderRawIndexService(model);
    }

    @RequestMapping(value = "/my-juragan/orderRaw/{id}/confirm")
    public String processOrderRaw(@PathVariable long id)
    {
        return juraganService.processOrderRawService(id);
    }

    @RequestMapping(value = "/my-juragan/orderRaw/{id}/delete", method = RequestMethod.POST)
    public String deleteOrderRaw(@PathVariable long id)
    {
        return juraganService.deleteOrderRawService(id);
    }

    @RequestMapping(value = "/my-juragan/order", method = RequestMethod.GET)
    public String orderIndex(
        Model model,
        HttpServletRequest request)
    {
        return juraganService.orderIndexService(model, request);
    }

    @RequestMapping(value = "/my-juragan/order/new", method = RequestMethod.POST)
    public String createNewOrder(
        HttpServletRequest request)
    {
        return juraganService.createNewOrderService(request);
    }

    @RequestMapping(value = "/my-juragan/order/{id}/cart", method = RequestMethod.GET)
    public String orderCart(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        return juraganService.orderCartService(id, model, request);
    }

    @RequestMapping(value = "/my-juragan/order/{id}/confirm", method = RequestMethod.POST)
    public String checkoutOrder(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        return juraganService.checkoutOrderService(model, request, id);
    }

    @RequestMapping(value = "/my-juragan/order/{id}/nego",method = RequestMethod.POST)
    public String negoOrder(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        return juraganService.negoOrderService(id, model, request);
    }

    @RequestMapping(value = "/my-juragan/order/{id}/delete", method = RequestMethod.POST)
    public String deleteOrder(
        Model model,
        @PathVariable int id)
    {
        return juraganService.deleteOrderService(model, id);
    }
}

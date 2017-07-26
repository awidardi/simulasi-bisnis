package com.kkn.masaran.service.service;

import com.kkn.masaran.model.Businessman;
import com.kkn.masaran.model.Product;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ARDI on 6/22/2017.
 */

@Service
public interface BusinessmanService {

    public String businessmanRegisterGetService(Model model, HttpServletRequest request);

    public String businessmanRegisterPostService(@ModelAttribute Businessman businessman, Model model, HttpServletRequest request);

    public String umkmProfileService(Model model, HttpServletRequest request);

    public String umkmGetAddProductService(
        Model model,
        HttpServletRequest request);

    public String umkmPostAddProductService(
        @ModelAttribute Product newProduct,
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request);

    public String editProductGetService(
        @PathVariable long id,
        HttpServletRequest request,
        Model model);

    public String editProductPostService(
        @RequestParam("file") MultipartFile file,
        @PathVariable long id,
        HttpServletRequest request);

    public String deleteProductService(
        @PathVariable long id);

    public String sellNewProductService(
        @PathVariable long id,
        HttpServletRequest request);

    public String sellProductCancelService(
        @PathVariable long id,
        HttpServletRequest request);

    public String orderRawIndexService(
        Model model,
        HttpServletRequest request);

    public String createNewOrderRawService(HttpServletRequest request);

    public String orderRawCartService(
        @PathVariable int id,
        Model model,
        HttpServletRequest request);

    public String checkoutOrderRawService(
        Model model,
        HttpServletRequest request,
        @PathVariable int id);

    public String deleteOrderRawService(
        Model model,
        @PathVariable int id);

    public String editUmkmProfileGetService(
        HttpServletRequest request,
        Model model);

    public String editUmkmProfilePostService(
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request);

    public String orderIndexService(
        Model model,
        HttpServletRequest request);

    public String processOrderService(@PathVariable long id);

    public String deleteOrderService(@PathVariable long id);

    public String negoOrderService(
        HttpServletRequest request,
        @PathVariable long id);
}

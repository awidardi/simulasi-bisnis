package com.kkn.masaran.service.service;

import com.kkn.masaran.model.Businessman;
import com.kkn.masaran.model.Juragan;
import com.kkn.masaran.model.RawMaterial;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.security.access.method.P;
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
public interface JuraganService {

    public String juraganRegisterGetService(Model model, HttpServletRequest request);

    public String juraganRegisterPostService(@ModelAttribute Juragan juragan, Model model, HttpServletRequest request);

    public String juraganProfileService(Model model, HttpServletRequest request);

    public String juraganGetAddProductService(
        Model model,
        HttpServletRequest request);

    public String juraganPostAddProductService(
        @ModelAttribute RawMaterial rawMaterial,
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request);

    public String editRawMaterialGetService(
        @PathVariable long id,
        HttpServletRequest request,
        Model model);

    public String editRawMaterialPostService(
        @RequestParam("file") MultipartFile file,
        @PathVariable long id,
        HttpServletRequest request);

    public String deleteRawMaterialService(
        @PathVariable long id);

    public String addPromoGetService(
        @PathVariable long id,
        HttpServletRequest request,
        Model model);

    public String addPromoPostService(
        HttpServletRequest request,
        @PathVariable long id);

    public String stopPromoService(
    @PathVariable long id);

    public String orderRawIndexService(Model model);

    public String processOrderRawService(@PathVariable long id);

    public String deleteOrderRawService(@PathVariable long id);

    public String orderIndexService(
        Model model,
        HttpServletRequest request);

    public String createNewOrderService(HttpServletRequest request);

    public String orderCartService(
        @PathVariable int id,
        Model model,
        HttpServletRequest request);

    public String checkoutOrderService(
        Model model,
        HttpServletRequest request,
        @PathVariable int id);

    public String negoOrderService(
        @PathVariable int id,
        Model model,
        HttpServletRequest request);

    public String deleteOrderService(
        Model model,
        @PathVariable int id);
}

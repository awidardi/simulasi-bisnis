package com.kkn.masaran.service.impl;

import com.kkn.masaran.model.*;
import com.kkn.masaran.repository.*;
import com.kkn.masaran.service.service.BusinessmanService;
import com.kkn.masaran.service.service.JuraganService;
import com.kkn.masaran.utility.Helper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ARDI on 6/22/2017.
 */

@Service
public class JuraganServiceImpl implements JuraganService {

    @Autowired
    private Environment env;
    @Autowired
    private JuraganRepository juraganRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private Helper helper;

    @Autowired
    private OrderRawRepository orderRawRepository;
    @Autowired
    private OrderRawDetailRepository orderRawDetailRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory
    .getLogger(JuraganServiceImpl.class);

    @ModelAttribute("helper")
    public Helper getHelper() {
        return helper;
    }

    @ModelAttribute("activeUser")
    public User getActiveUser() {
        return helper.getCurrentUser();
    }

    @Override
    public String juraganRegisterGetService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        return "customer/register";
    }

    @Override
    public String juraganRegisterPostService(
        @ModelAttribute Juragan juragan,
        Model model,
        HttpServletRequest request)
    {
        juraganRepository.save(juragan);
        UserRole r = new UserRole();
        r.setUsername(juragan.getUsername());
        r.setRole("ROLE_JURAGAN");
        userRoleRepository.save(r);

        System.out.print("masuk");

        helper.authenticateUserAndSetSession(juragan, request);

        return "redirect:/my-juragan/profile";
    }

    @Override
    public String juraganProfileService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        Juragan juragan = (Juragan) helper.getCurrentUser();
        model.addAttribute("juragan", juragan);
        return "customer/profile";
    }

    @Override
    public String juraganGetAddProductService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        model.addAttribute("juragan", helper.getCurrentJuragan());

        return "customer/addproducts";
    }

    @Override
    public String juraganPostAddProductService(
        @ModelAttribute RawMaterial rawMaterial,
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
    {
        Juragan juragan = helper.getCurrentJuragan();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        if(juragan != null){
            if(!juragan.hasRawMaterial(rawMaterial)){
                rawMaterial.setJuragan(juragan);
            }
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();

                    String fileName = UUID.randomUUID().toString().replaceAll("-","");

                    // Creating the directory to store file
                    //String rootPath = System.getProperty("catalina.home");
                    File dir = new File(env.getProperty("simulasi-bisnis.PhotoDir.path") + "/RawMaterial/" + formatted);
                    if (!dir.exists())
                        dir.mkdirs();

                    // Create the file on server
                    File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + fileName + ".jpg");
                    rawMaterial.setPhoto("http://localhost/gambar-simulasi-kkn/RawMaterial"
                    //rawMaterial.setPhoto("http://169.254.241.226/gambar-simulasi-kkn/RawMaterial"
                    //rawMaterial.setPhoto("/assets/image/gambar-simulasi-kkn/RawMaterial"
                    + File.separator + formatted + File.separator + fileName + ".jpg");
                    BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();

                    logger.info("Server File Location="
                    + serverFile.getAbsolutePath());

                } catch (Exception e) {
                    return "You failed to upload " + rawMaterial.getName() + " => " + e.getMessage();
                }
            } else {
                return "You failed to upload " + rawMaterial.getName()
                + " because the file was empty.";
            }

            String[] quantity = request.getParameterValues("quantity");
            String[] price = request.getParameterValues("price");
            ArrayList<Pair<Integer, Integer>> pricePair = new ArrayList<>();
            for(int i=0;i<quantity.length;i++){
                pricePair.add(Pair.of(Integer.parseInt(quantity[i]) , Integer.parseInt(price[i])));
            }

            Collections.sort(pricePair, new Comparator<Pair<Integer,Integer>>() {
                @Override public int compare(Pair<Integer,Integer> x, Pair<Integer,Integer> y) {
                    return x.getFirst() - y.getFirst();
                }
            });

            for(int i=0;i<pricePair.size();i++){
                quantity[i] = String.valueOf(pricePair.get(i).getFirst());
                price[i] = String.valueOf(pricePair.get(i).getSecond());
            }

            String newProductPrice = helper.setProductPrice(quantity,price);
            rawMaterial.setPrice(newProductPrice);
            String stock = request.getParameter("stock");
            rawMaterial.setStock(Integer.parseInt(stock));

            rawMaterialRepository.save(rawMaterial);
            juraganRepository.save(juragan);
            return "redirect:/my-juragan/profile";
        }
        return "redirect:/my-juragan/profile";
    }

    @Override
    public String editRawMaterialGetService(
        @PathVariable long id,
        HttpServletRequest request,
        Model model)
    {
        RawMaterial rawMaterial = rawMaterialRepository.findOne(id);
        model.addAttribute("rawMaterial", rawMaterial);
        ArrayList<Pair<Integer , Integer >> rawMaterialPriceList = rawMaterial.getPricePair();
        model.addAttribute("rawMaterialPriceList",rawMaterialPriceList);
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        return "customer/editProduct";
    }

    @Override
    public String editRawMaterialPostService(
        @RequestParam("file") MultipartFile file,
        @PathVariable long id,
        HttpServletRequest request)
    {
        Juragan juragan = (Juragan) helper.getCurrentUser();

        RawMaterial rawMaterial = rawMaterialRepository.findOne(id);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        if(juragan != null){
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();

                    String fileName = UUID.randomUUID().toString().replaceAll("-","");

                    // Creating the directory to store file
                    //String rootPath = System.getProperty("catalina.home");
                    File dir = new File(env.getProperty("simulasi-bisnis.PhotoDir.path") + "/RawMaterial/" + formatted);
                    if (!dir.exists())
                        dir.mkdirs();

                    // Create the file on server
                    File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + fileName + ".jpg");
                    BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    //rawMaterial.setPhoto("http://169.254.241.226/gambar-simulasi-kkn/RawMaterial"
                    rawMaterial.setPhoto("http://localhost/gambar-simulasi-kkn/RawMaterial"
                    //rawMaterial.setPhoto("/assets/image/gambar-simulasi-kkn/RawMaterial"
                    + File.separator + formatted + File.separator + fileName + ".jpg");

                    logger.info("Server File Location="
                    + serverFile.getAbsolutePath());

                } catch (Exception e) {
                    return "You failed to upload " + rawMaterial.getName() + " => " + e.getMessage();
                }
            }
            rawMaterial.setDescription(request.getParameter("description"));
            rawMaterial.setName(request.getParameter("name"));
            String[] quantity = request.getParameterValues("quantity");
            String[] price = request.getParameterValues("price");
            ArrayList<Pair<Integer, Integer>> pricePair = new ArrayList<>();
            for(int i=0;i<quantity.length;i++){
                pricePair.add(Pair.of(Integer.parseInt(quantity[i]) , Integer.parseInt(price[i])));
            }

            Collections.sort(pricePair, new Comparator<Pair<Integer,Integer>>() {
                @Override public int compare(Pair<Integer,Integer> x, Pair<Integer,Integer> y) {
                    return x.getFirst() - y.getFirst();
                }
            });

            for(int i=0;i<pricePair.size();i++){
                quantity[i] = String.valueOf(pricePair.get(i).getFirst());
                price[i] = String.valueOf(pricePair.get(i).getSecond());
            }

            String newProductPrice = helper.setProductPrice(quantity,price);
            rawMaterial.setPrice(newProductPrice);
            String stock = request.getParameter("stock");
            rawMaterial.setStock(Integer.parseInt(stock));
            if(Integer.parseInt(stock) > 0){
                rawMaterial.setStockExist(true);
            }else if(Integer.parseInt(stock) == 0){
                rawMaterial.setStockExist(false);
            }
            rawMaterialRepository.save(rawMaterial);
            juraganRepository.save(juragan);
            return "redirect:/my-juragan/profile";
        }
        return "redirect:/my-juragan/profile";
    }

    @Override
    public String deleteRawMaterialService(
        @PathVariable long id)
    {
        RawMaterial rawMaterial = rawMaterialRepository.findOne(id);
        rawMaterialRepository.delete(rawMaterial);

        return "redirect:/my-juragan/profile";
    }

    @Override
    public String addPromoGetService(
        @PathVariable long id,
        HttpServletRequest request,
        Model model)
    {
        RawMaterial rawMaterial = rawMaterialRepository.findOne(id);
        model.addAttribute("rawMaterial", rawMaterial);
        ArrayList<Pair<Integer , Integer >> rawMaterialPriceList = rawMaterial.getPricePair();
        model.addAttribute("rawMaterialPriceList",rawMaterialPriceList);
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        return "customer/addPromo";
    }

    @Override
    public String addPromoPostService(
        HttpServletRequest request,
        @PathVariable long id)
    {
        RawMaterial rawMaterial = rawMaterialRepository.findOne(id);
        String newPromo = request.getParameter("promo");
        rawMaterial.setPromo(Integer.parseInt(newPromo));
        rawMaterial.setDiscounted(true);
        rawMaterialRepository.save(rawMaterial);

        return "redirect:/my-juragan/profile";
    }

    @Override
    public String stopPromoService(
        @PathVariable long id)
    {
        RawMaterial rawMaterial = rawMaterialRepository.findOne(id);
        rawMaterial.setDiscounted(false);
        rawMaterial.setPromo(0);
        rawMaterialRepository.save(rawMaterial);

        return "redirect:/my-juragan/profile";
    }

    @Override
    public String orderRawIndexService(Model model) {
        List<OrderRaw> orderRawList = orderRawRepository.findByJuraganUsernameAndStatus(helper.getCurrentUser().getUsername(), OrderRaw.ORDER_STATUS_PENDING);
        orderRawList.addAll(orderRawRepository.findByJuraganUsernameAndStatus(helper.getCurrentUser().getUsername(), OrderRaw.ORDER_STATUS_WAITING));
        orderRawList.addAll(orderRawRepository.findByJuraganUsernameAndStatus(helper.getCurrentUser().getUsername(), OrderRaw.ORDER_STATUS_COMPLETE));

        model.addAttribute("orderRawList", orderRawList);
        return "customer/orderRaw";
    }

    @Override
    public String processOrderRawService(@PathVariable long id) {
        OrderRaw orderRaw = orderRawRepository.findOne(id);
        if (orderRaw.isPending()) {
            orderRaw.setStatus(Order.ORDER_STATUS_WAITING);
        } else if (orderRaw.isWaiting()) {
            orderRaw.setStatus(Order.ORDER_STATUS_COMPLETE);
            orderRaw.getBusinessman().setBalance
                (orderRaw.getBusinessman().getBalance() - orderRaw.getTotalPrices());

            for(OrderRawDetail orderRawDetail: orderRaw.getOrderRawDetails()){
                RawMaterial rawMaterial = orderRawDetail.getRawMaterial();
                rawMaterial.setStock(rawMaterial.getStock() - orderRaw.getQuantities());
                if(rawMaterial.getStock() - orderRaw.getQuantities() <= 0){
                    rawMaterial.setStockExist(false);
                }
                rawMaterialRepository.save(rawMaterial);
            }
        }
        orderRawRepository.save(orderRaw);
        return "redirect:/my-juragan/orderRaw";
    }

    @Override
    public String deleteOrderRawService(@PathVariable long id) {
        orderRawRepository.delete(id);
        return "redirect:/my-juragan/orderRaw";
    }

    @Override
    public String orderIndexService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        Juragan juragan = helper.getCurrentJuragan();
        List<Order> orderList = juragan.getOrders();
        model.addAttribute("orderList", orderList);
        model.addAttribute("juragan", juragan);
        return "customer/order";
    }

    @Override
    public String createNewOrderService(HttpServletRequest request) {
        Order order = new Order();

        for(String productId: request.getParameterValues("choosen-product")) {
            Product orderedProduct = productRepository.findOne(Long.parseLong(productId));

            int orderQuantity = Integer.parseInt(request.getParameter("quantity"));
            Businessman businessman = orderedProduct.getBusinessman();
            order.setJuragan(helper.getCurrentJuragan());
            order.setBusinessman(businessman);
            order.setQuantities(orderQuantity);
            order.setNote(request.getParameter("note"));
            orderRepository.save(order);


            OrderDetail od = new OrderDetail(order, orderedProduct);
            orderDetailRepository.save(od);
        }
        order.updateTotalPrices();
        orderRepository.save(order);

        return "redirect:/my-juragan/order/" + order.getId() + "/cart";
    }

    @Override
    public String orderCartService(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        model.addAttribute("juragan", helper.getCurrentJuragan());
        model.addAttribute("order", orderRepository.findOne((long) id));

        return "customer/cart";
    }

    @Override
    public String checkoutOrderService(
        Model model,
        HttpServletRequest request,
        @PathVariable int id)
    {
        Order order = orderRepository.findOne((long) id);
        order.setStatus(Order.ORDER_STATUS_COMPLETE);

        long currentBalance = order.getBusinessman().getBalance();
        long totalPrices;
        if(order.getHargaNego()==0) totalPrices = order.getTotalPrices();
        else totalPrices = order.getHargaNego();
        order.getBusinessman().setBalance(currentBalance + totalPrices);

        for(OrderDetail od: order.getOrderDetails()) {
            Product product = od.getProduct();
            product.setStock(product.getStock() - order.getQuantities());

            if(product.getStock() - order.getQuantities()<=0){
                product.setSold(false);
            }

            productRepository.save(product);
        }

        orderRepository.save(order);
        return "redirect:/my-juragan/order";
    }

    @Override
    public String negoOrderService(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        Order order = orderRepository.findOne((long) id);
        order.setHargaNego(Long.parseLong(request.getParameter("nego")));

        order.setStatus(Order.ORDER_STATUS_PENDING);

        orderRepository.save(order);

        return "redirect:/my-juragan/order";
    }

    @Override
    public String deleteOrderService(
        Model model,
        @PathVariable int id)
    {
        orderRepository.delete((long) id);
        return "redirect:/my-juragan/order";
    }

}

package com.kkn.masaran.service.impl;

import com.kkn.masaran.model.*;
import com.kkn.masaran.repository.*;
import com.kkn.masaran.service.service.BusinessmanService;
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

@Service
public class BusinessmanServiceImpl implements BusinessmanService {

    @Autowired
    private Environment env;
    @Autowired
    private BusinessmanRepository businessmanRepository;
    @Autowired
    private JuraganRepository juraganRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private Helper helper;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRawRepository orderRawRepository;
    @Autowired
    private OrderRawDetailRepository orderRawDetailRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    private static final Logger logger = LoggerFactory
                .getLogger(BusinessmanServiceImpl.class);

    @ModelAttribute("helper")
    public Helper getHelper() {
        return helper;
    }

    @ModelAttribute("activeUser")
    public User getActiveUser() {
        return helper.getCurrentUser();
    }

    @Override
    public String businessmanRegisterGetService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        return "catering/register";
    }

    @Override
    public String businessmanRegisterPostService(
        @ModelAttribute Businessman businessman,
        Model model,
        HttpServletRequest request)
    {
        businessmanRepository.save(businessman);
        UserRole r = new UserRole();
        r.setUsername(businessman.getUsername());
        r.setRole("ROLE_UMKM");
        userRoleRepository.save(r);

        System.out.print("masuk");

        helper.authenticateUserAndSetSession(businessman, request);

        return "redirect:/my-umkm/profile";
    }

    @Override
    public String umkmProfileService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        Businessman businessman = (Businessman) helper.getCurrentUser();
        model.addAttribute("businessman", businessman);
        return "catering/profile";
    }

    @Override
    @RequestMapping(value="/my-umkm/addproducts",method= RequestMethod.GET)
    public String umkmGetAddProductService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        model.addAttribute("businessman", helper.getCurrentBusinessman());

        return "catering/addproducts";
    }

    @Override
    @RequestMapping(value="/my-umkm/addproducts", method=RequestMethod.POST)
    public String umkmPostAddProductService(
        @ModelAttribute Product newProduct,
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
    {
        Businessman businessman = helper.getCurrentBusinessman();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        if(businessman != null){
            if(!businessman.hasProduct(newProduct)){
                newProduct.setBusinessman(businessman);
            }
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();

                    String fileName = UUID.randomUUID().toString().replaceAll("-","");

                    // Creating the directory to store file
                    //String rootPath = System.getProperty("catalina.home");
                    File dir = new File(env.getProperty("simulasi-bisnis.PhotoDir.path") + "/Product/" + formatted);
                    if (!dir.exists())
                        dir.mkdirs();

                    // Create the file on server
                    File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + fileName + ".jpg");
                    newProduct.setPhoto("http://localhost/gambar-simulasi-kkn/Product"
                    //newProduct.setPhoto("http://169.254.241.226/gambar-simulasi-kkn/Product"
                    //newProduct.setPhoto("/assets/image/gambar-simulasi-kkn/Product"
                    + File.separator + formatted + File.separator + fileName + ".jpg");
                    BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();

                    logger.info("Server File Location="
                    + serverFile.getAbsolutePath());

                } catch (Exception e) {
                    return "You failed to upload " + newProduct.getName() + " => " + e.getMessage();
                }
            } else {
                return "You failed to upload " + newProduct.getName()
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
            newProduct.setPrice(newProductPrice);
            String stock = request.getParameter("stock");
            newProduct.setStock(Integer.parseInt(stock));

            productRepository.save(newProduct);
            businessmanRepository.save(businessman);
            return "redirect:/my-umkm/profile";
        }
        return "redirect:/my-umkm/profile";
    }

    @Override
    public String editProductGetService(
        @PathVariable long id,
        HttpServletRequest request,
        Model model)
    {
        Product product = productRepository.findOne(id);
        model.addAttribute("product", product);
        ArrayList<Pair<Integer , Integer >> productPriceList = product.getPricePair();
        model.addAttribute("productPriceList",productPriceList);
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        return "catering/editProduct";
    }

    @Override
    public String editProductPostService(
        @RequestParam("file") MultipartFile file,
        @PathVariable long id,
        HttpServletRequest request)
    {
        Businessman businessman = (Businessman) helper.getCurrentUser();

        Product product = productRepository.findOne(id);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        if(businessman != null){
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();

                    String fileName = UUID.randomUUID().toString().replaceAll("-","");

                    // Creating the directory to store file
                    //String rootPath = System.getProperty("catalina.home");
                    File dir = new File(env.getProperty("simulasi-bisnis.PhotoDir.path") + "/Product/" + formatted);
                    if (!dir.exists())
                        dir.mkdirs();

                    // Create the file on server
                    File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + fileName + ".jpg");
                    BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    product.setPhoto("http://localhost/gambar-simulasi-kkn/Product"
                    //product.setPhoto("http://169.254.241.226/gambar-simulasi-kkn/Product"
                    //product.setPhoto("/assets/image/gambar-simulasi-kkn/Product"
                    + File.separator + formatted + File.separator + fileName + ".jpg");

                    logger.info("Server File Location="
                    + serverFile.getAbsolutePath());

                } catch (Exception e) {
                    return "You failed to upload " + product.getName() + " => " + e.getMessage();
                }
            }
            product.setDescription(request.getParameter("description"));
            product.setName(request.getParameter("name"));
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

            String stock = request.getParameter("stock");
            product.setStock(Integer.parseInt(stock));

            String newProductPrice = helper.setProductPrice(quantity,price);
            product.setPrice(newProductPrice);
            productRepository.save(product);
            businessmanRepository.save(businessman);
            return "redirect:/my-umkm/profile";
        }
        return "redirect:/my-umkm/profile";
    }

    @Override
    public String deleteProductService(@PathVariable long id) {

        Product product = productRepository.findOne(id);
        productRepository.delete(product);

        return "redirect:/my-umkm/profile";
    }

    @Override
    public String sellNewProductService(
        @PathVariable long id,
        HttpServletRequest request)
    {
        Product product = productRepository.findOne(id);
        product.setSold(true);
        productRepository.save(product);

        return "redirect:/my-umkm/profile";
    }

    @Override
    public String sellProductCancelService(@PathVariable long id, HttpServletRequest request) {
        Product product = productRepository.findOne(id);
        product.setSold(false);
        productRepository.save(product);

        return "redirect:/my-umkm/profile";
    }

    @Override
    public String orderRawIndexService(
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        Businessman businessman = helper.getCurrentBusinessman();
        List<OrderRaw> orderRawList = businessman.getOrderRaws();
        model.addAttribute("orderRawList", orderRawList);
        model.addAttribute("businessman", businessman);
        return "catering/orderRaw";
    }

    @Override
    public String createNewOrderRawService(HttpServletRequest request) {
        OrderRaw orderRaw = new OrderRaw();

        for(String productId: request.getParameterValues("choosen-product")) {
            RawMaterial orderedProduct = rawMaterialRepository.findOne(Long.parseLong(productId));

            int orderQuantity = Integer.parseInt(request.getParameter("quantity"));
            Juragan juragan = orderedProduct.getJuragan();
            orderRaw.setBusinessman(helper.getCurrentBusinessman());
            orderRaw.setJuragan(juragan);
            orderRaw.setQuantities(orderQuantity);
            orderRaw.setNote(request.getParameter("note"));
            orderRawRepository.save(orderRaw);


            OrderRawDetail od = new OrderRawDetail(orderRaw, orderedProduct);
            orderRawDetailRepository.save(od);
        }
        orderRaw.updateTotalPrices();
        orderRawRepository.save(orderRaw);

        return "redirect:/my-umkm/orderRaw/" + orderRaw.getId() + "/cart";
    }

    @Override
    public String orderRawCartService(
        @PathVariable int id,
        Model model,
        HttpServletRequest request)
    {
        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);

        model.addAttribute("businessman", helper.getCurrentBusinessman());
        model.addAttribute("orderRaw", orderRawRepository.findOne((long) id));

        return "catering/cartRaw";
    }

    @Override
    public String checkoutOrderRawService(
        Model model,
        HttpServletRequest request,
        @PathVariable int id)
    {
        OrderRaw orderRaw = orderRawRepository.findOne((long) id);
        if(orderRaw.getStatus() == OrderRaw.ORDER_STATUS_CART){
            orderRaw.setStatus(OrderRaw.ORDER_STATUS_PENDING);
        }
        orderRawRepository.save(orderRaw);
        return "redirect:/my-umkm/orderRaw";
    }

    @Override
    public String deleteOrderRawService(
        Model model,
        @PathVariable int id)
    {
        orderRawRepository.delete((long) id);
        return "redirect:/my-umkm/orderRaw";
    }

    @Override
    public String editUmkmProfileGetService(
        HttpServletRequest request,
        Model model)
    {
        Businessman businessman = (Businessman) helper.getCurrentUser();
        model.addAttribute("businessman", businessman);

        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        return "catering/edit";
    }

    @Override
    public String editUmkmProfilePostService(
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
    {
        Businessman businessman = helper.getCurrentBusinessman();

        //photo
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        if(businessman != null){
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();

                    String fileName = UUID.randomUUID().toString().replaceAll("-","");

                    // Creating the directory to store file
                    File dir = new File(env.getProperty("simulasi-bisnis.PhotoDir.path") + "/Umkm/" + formatted);
                    if (!dir.exists())
                        dir.mkdirs();

                    // Create the file on server
                    File serverFile = new File(dir.getAbsolutePath()
                    + File.separator + fileName + ".jpg");
                    businessman.setPhoto("http://localhost/gambar-simulasi-kkn/Umkm"
                    //businessman.setPhoto("http://169.254.241.226/gambar-simulasi-kkn/Umkm"
                    //businessman.setPhoto("/assets/image/gambar-simulasi-kkn/Umkm"
                    + File.separator + formatted + File.separator + fileName + ".jpg");
                    BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();

                    logger.info("Server File Location="
                    + serverFile.getAbsolutePath());

                } catch (Exception e) {
                    return "You failed to upload " + businessman.getBusinessName() + " => " + e.getMessage();
                }
            }
        }
        businessman.setPassword(request.getParameter("password"));
        businessman.setEmail(request.getParameter("email"));
        businessman.setBusinessName(request.getParameter("businessName"));
        businessman.setAddress(request.getParameter("address"));
        businessman.setDescription(request.getParameter("description"));
        businessman.setPhoneNumber(request.getParameter("phoneNumber"));

        businessmanRepository.save(businessman);
        return "redirect:/my-umkm/profile";
    }

    @Override
    public String orderIndexService(
        Model model,
        HttpServletRequest request)
    {
        List<Order> orderList = orderRepository.findByBusinessmanUsernameAndStatus(helper.getCurrentUser().getUsername(), Order.ORDER_STATUS_PENDING);
        orderList.addAll(orderRepository.findByBusinessmanUsernameAndStatus(helper.getCurrentUser().getUsername(), Order.ORDER_STATUS_WAITING));
        orderList.addAll(orderRepository.findByBusinessmanUsernameAndStatus(helper.getCurrentUser().getUsername(), Order.ORDER_STATUS_COMPLETE));

        String _csrf = ((CsrfToken) request.getAttribute("_csrf")).getToken();
        model.addAttribute("_csrf", _csrf);
        model.addAttribute("orderList", orderList);
        return "catering/order";
    }

    @Override
    public String processOrderService(@PathVariable long id) {
        Order order = orderRepository.findOne(id);

        order.setStatus(Order.ORDER_STATUS_COMPLETE);

        for(OrderDetail od: order.getOrderDetails()) {
            Product product = od.getProduct();
            product.setStock(product.getStock() - order.getQuantities());
        }

        long currentBalance = order.getBusinessman().getBalance();
        long totalPrices;
        if(order.getHargaNego()==0) totalPrices = order.getTotalPrices();
        else totalPrices = order.getHargaNego();
        order.getBusinessman().setBalance(currentBalance + totalPrices);

        orderRepository.save(order);
        return "redirect:/my-umkm/order";
    }

    @Override
    public String deleteOrderService(@PathVariable long id) {
        orderRepository.delete(id);
        return "redirect:/my-umkm/order";
    }

    @Override
    public String negoOrderService(
        HttpServletRequest request,
        @PathVariable long id)
    {
        Order order = orderRepository.findOne((long) id);
        order.setHargaNego(Long.parseLong(request.getParameter("nego")));

        int banyakNego = order.getBanyakNego();
        order.setBanyakNego(banyakNego-1);
        if(banyakNego==1) order.setCanNego(false);

        order.setStatus(Order.ORDER_STATUS_WAITING);

        orderRepository.save(order);

        return "redirect:/my-umkm/order";
    }

}

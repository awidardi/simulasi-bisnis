package com.kkn.masaran.utility;

import com.kkn.masaran.model.Businessman;
import com.kkn.masaran.model.Juragan;
import com.kkn.masaran.model.User;
import com.kkn.masaran.repository.UserRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@Component
public class Helper {
    @Autowired
    UserRepository userRepository;
    @Autowired
    protected AuthenticationManager authenticationManager;

    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken) ;
    }

    public boolean isLoggedInAsJuragan() {
        if (isLoggedIn()) {
            User u = getCurrentUser();
            return u instanceof Juragan;
        }
        return false;
    }

    public boolean isLoggedInAsBusinessman() {
        if (isLoggedIn()) {
            User u = getCurrentUser();
            return u instanceof Businessman;
        }
        return false;
    }

    public User getCurrentUser() {
        if (isLoggedIn()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return userRepository.findByUsername(auth.getName());
        }
        return null;
    }

    public ArrayList<Pair<Integer, Integer>> getPricePair(String priceData) {
        ArrayList<Pair<Integer, Integer>> pricePair = new ArrayList<>();
        for (String stringPair: priceData.split("\\|")) {
            int minQuantity = Integer.parseInt(stringPair.split("\\-")[0]);
            int price = Integer.parseInt(stringPair.split("\\-")[1]);
            pricePair.add(Pair.of(minQuantity, price));
        }
        return pricePair;
    }

    public String setProductPrice(String[] quantity, String[] price) {
        String productPrice = "";
        int priceSize = quantity.length;
        for(int i=0;i<priceSize;i++){
            String productPriceTmp;
            if(i!=priceSize-1) productPriceTmp = quantity[i] + "-" + price[i] + "|";
            else productPriceTmp = quantity[i] + "-" + price[i];
            productPrice = productPrice + productPriceTmp;
        }
        return productPrice;
    }

    public Juragan getCurrentJuragan() {
        return (Juragan) getCurrentUser();
    }

    public Businessman getCurrentBusinessman() {
        return (Businessman) getCurrentUser();
    }

    public void authenticateUserAndSetSession(User user, HttpServletRequest request) {
        String username = user.getUsername();
        String password = user.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    /**
     * get targetURL from session
     */
    public String getRememberMeTargetUrlFromSession(HttpServletRequest request){
        String targetUrl = "";
        HttpSession session = request.getSession(false);
        if(session!=null){
            targetUrl = session.getAttribute("targetUrl")==null?""
            :session.getAttribute("targetUrl").toString();
        }
        return targetUrl;
    }
}

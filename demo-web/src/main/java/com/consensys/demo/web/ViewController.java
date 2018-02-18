package com.consensys.demo.web;

import com.consensys.demo.web.auth.Account;
import com.consensys.demo.web.content.ContentDTO;
import com.consensys.demo.web.content.ContentIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Calvin Ngo on 16/2/18.
 */

@Controller
public class ViewController {

    @Autowired
    private ContentIndexRepository indexRepository;

    @GetMapping("/")
    public String loginView() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupView() {
        return "signup";
    }

    @GetMapping("/app")
    public String imageView(Map<String, Object> model, Authentication authentication) {
        Account account = null;
        if(authentication != null) {
            account = (Account)authentication.getPrincipal();
        }
        model.put("isAuthenticated", account != null);
        model.put("username", account != null ? account.getUsername() : "Guest");

        List<ContentDTO> images = indexRepository.findByOwner(account).stream()
                .map(userContent -> new ContentDTO(userContent))
                .collect(Collectors.toList());
        model.put("images", images);

        return "app";
    }

}

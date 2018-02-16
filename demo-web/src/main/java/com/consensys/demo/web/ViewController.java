package com.consensys.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Calvin Ngo on 16/2/18.
 */

@Controller
public class ViewController {

    @GetMapping("/")
    public String loginView() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupView() {
        return "signup";
    }

    @GetMapping("/signup-success")
    public String signupComplete() {
        return "signupSuccess";
    }
}

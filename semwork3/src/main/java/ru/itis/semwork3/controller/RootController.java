//package ru.itis.semwork3.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.security.PermitAll;
//
//@Controller
//@RequestMapping("/")
//public class RootController {
//
//    @GetMapping
//    public String getRoot(Authentication authentication) {
//        if (authentication != null) {
//            return "redirect:/profile";
//        } else {
//            return "redirect:/signIn";
//        }
//    }
//}

package kr.co.sboard.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/index"})
    public String index(Authentication authentication){

        if(authentication.isAuthenticated()){
            return "forward:/article/list";
        }else{
            return "forward:/user/login";
        }


    }

}
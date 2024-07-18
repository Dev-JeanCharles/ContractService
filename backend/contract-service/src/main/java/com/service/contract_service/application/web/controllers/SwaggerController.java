package com.service.contract_service.application.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class SwaggerController {

    @GetMapping("/docs")
    public String swaggerRedirect() {
        return "redirect:/swagger-ui.html";
    }
}

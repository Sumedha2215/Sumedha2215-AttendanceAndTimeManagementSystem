package com.timeattendance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html"; // ✅ Serves the static file directly
    }
}

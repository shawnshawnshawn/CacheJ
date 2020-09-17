package com.aio.cacheJ;

import com.aio.cacheJ.anno.CacheClear;
import com.aio.cacheJ.anno.CacheValue;
import com.aio.cacheJ.intercepter.CacheJSpelExpressionParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }

    @CacheValue(key = "'test:' + #param")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(String param) {
        return "success";
    }

    @CacheClear(key = "'test:' + #param")
    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(String param) {
        return "success";
    }

}

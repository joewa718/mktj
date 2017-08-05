package com.mktj.cn.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/session")
@Scope("prototype")
public class HttpSessionController extends BaseController {
    @RequestMapping(value = "/expire", method = RequestMethod.GET)
    public void expire(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase(
                "XMLHttpRequest")) { // ajax 超时处理
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            response.sendRedirect("/");
        }

    }
}

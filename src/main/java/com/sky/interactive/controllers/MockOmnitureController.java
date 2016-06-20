package com.sky.interactive.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MockOmnitureController {

    public static final String SUCCESS_RESPONSE_BODY = "<status>SUCCESS</status>";
    public static final String FAILURE_RESPONSE_BODY = "<status>SYNTAX_ERROR</status>";

    static final String SUCCESS_VARIABLE_NAME = "success";
    static final String DELAY_VARIABLE_NAME = "delay";

    @RequestMapping(value = "/success/{" + SUCCESS_VARIABLE_NAME + "}/delay/{" + DELAY_VARIABLE_NAME + "}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getAnswer(@PathVariable(SUCCESS_VARIABLE_NAME) boolean success, @PathVariable(DELAY_VARIABLE_NAME) int delayInMilliseconds) throws InterruptedException {
        Thread.sleep(delayInMilliseconds);
        if (success) {
            return SUCCESS_RESPONSE_BODY;
        } else {
            return FAILURE_RESPONSE_BODY;
        }
    }
}

package edu.example.sample.controller;

import edu.example.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    @GetMapping("/hello")
    public String hello() {
        sampleService.sampleMethod();
        return "Hello World!~";
        //http://localhost:8080/api/v1/sample/hello
    }
    @GetMapping("/hellos")
    public String[] hellos() {
        return new String[]{"Hello World!~" , "Hello World!~"};

    }

}
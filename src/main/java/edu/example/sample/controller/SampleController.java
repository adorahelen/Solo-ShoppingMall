package edu.example.sample.controller;

import edu.example.sample.service.SampleService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
@Log4j2
// 4. 문서화 제외
@Hidden
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;

    //@PreAuthorize("hasRole('USER')")
    // @PreAuthorize 해즈롤이 자동으로 붙는다?
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        log.info("---list()");
        return ResponseEntity.ok(new String[] {"AAA", "BBB", "CCC"});
    }


    @GetMapping("/hello")
    public String hello() {
        sampleService.sampleMethod();
        return "Hello World!~";
        //http://localhost:8080/api/v1/sample/hello
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hellos")
    public String[] hellos() {
        return new String[]{"Hello" , "HI"};

    }

}

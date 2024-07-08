package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class DictController {

    @PostMapping("/excelImportDict")
    public void addFromExcel(@RequestBody MultipartFile file, String versionCode, HttpServletResponse response){

    }

}

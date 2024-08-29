package edu.example.restz.controller;

import edu.example.restz.exception.UploadNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/files")
public class FileController {


    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile( /// Method 1 : 업로드 파일
            @RequestParam("files")MultipartFile[] files) {

        log.info("=== uploading files() ===" + files);

        if(files[0].isEmpty()) { // files.lenth == 0
            throw new UploadNotSupportedException("업로드 된 파일이 없습니다.");
        }


        for(MultipartFile file : files) {
            log.info("-----------------------------------------------------");
            log.info("name: " + file.getName());
            log.info("Origin name  === : " + file.getOriginalFilename());
            log.info("Type === : " + file.getContentType());
            checkFilExt(file.getOriginalFilename()); // 확장자 체크 메서드 정의한거 호출

        }

        return null;
    }

    // 업로드 파일 확장자 체크
    public void checkFilExt(String fileName)throws UploadNotSupportedException { // 예외를 던짐
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1); // 이미지 파일 확장자
        String regExp = "^(jpg|jpeg|JPG|JPEG|PNG|png|gif|GIF|bmp|BMP)";

        if(! ext.matches(regExp)) { throw
                new UploadNotSupportedException(" 지원되지 않는 파일 형식 입니다 : " + ext);
                // 예외를 생성
        }
        //업로드 파일의 확장자가 위에 해당하지 않는 경우
        // UploadNotSupportedException 예외 발생 시켜서 호출한 쪽에서 처리하도록 지정 
        // 메시지 - 지원하지 않는 형식입니다. : 확장자
    }
}
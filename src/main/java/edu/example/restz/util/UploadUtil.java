package edu.example.restz.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class UploadUtil {// 실제 업로드 수행

    @Value("${edu.example.upload.path}") // application.properties 파일에서 업로드 설정 경로 읽어오기
    private String uploadPath;

    @PostConstruct // 객체 생성 후 자동 실행 메서드 지정
    public void init() {  // 업로드 경로 실제 여부 확인 및 처리

        File tempDir = new File(uploadPath);

        if (!tempDir.exists()) { // 업로드 경로가 존재하지 않으면
            log.info("--- tempDir : " + tempDir);
            tempDir.mkdirs(); // 업로드 디렉토리 생성
        }

        uploadPath = tempDir.getAbsolutePath();
        log.info("--- getPath() :" + tempDir.getPath());
        log.info("--- getAbsolutePath() :" + tempDir.getAbsolutePath());
        log.info("--- uploadPath : " + uploadPath);
        log.info("-------------------------------");

    }

    // 업로드 수행
    public List<String> upload(MultipartFile[] files) {// 1. 컨트롤러에 전달된 업로드 파일들을 매개변수로 받는다.
      List<String> filenames = new ArrayList<>();
        for(MultipartFile file : files) {
            // 2. 전달받은 파일이 image 타입이 아닌 경우
            if (file.getContentType().startsWith("image")== false) {
                log.error("---- 지원하지 않는 파일 타입 : " + file.getContentType()); // 지원하지 않는 파일 타입이라 출력

                continue; // 다음 파일 확인
            }
            String uuid = UUID.randomUUID().toString(); // 랜덤으로 uuid 만든다. 앞에 붙이기 위해서
            String saveFilename = uuid + "_" + file.getOriginalFilename(); // 3. 이미지 파일명이 중복되지 않게 파일명에 uuid_를 결합하여
            String savePath = uploadPath + File.separator; // 4. 업로드 경로에 파일 구분자 File.separator 를 결합하여 저장

            try {
                file.transferTo(new File(savePath + saveFilename)); //--- 실제 파일 업로드 처리 부분---
                // 5. 업로드된 파일명을 List 객체에 저장한다.

                // 5-2 썸네일 파일 생성
                Thumbnails.of(new File(savePath + saveFilename))
                        .size(150, 150)
                                .toFile(savePath + "s_" + saveFilename);

                filenames.add(saveFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return filenames; // 5, 업로드된 파일명을 List 객체에 저장하여 반환
    }
    // 6. 파일 컨트롤러에서, 전달받은 값을 상태 코드 200으로 반환
    // 업로드 파일 삭제
    public void deleteFile(String filename) { // 1. 컨트롤러에서 삭제할 파일명을 매개변수로 받는다.
        File file = new File(uploadPath + File.separator + filename);
        File thumbFile = new File(uploadPath + File.separator + "s_" + filename);

        try {
            if (file.exists()) {
                file.delete();
            }
            if (thumbFile.exists()) {
                thumbFile.delete();
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }



}

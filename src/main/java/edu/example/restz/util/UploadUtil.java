package edu.example.restz.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
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




}

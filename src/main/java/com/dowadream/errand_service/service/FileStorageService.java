package com.dowadream.errand_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 파일 저장 관련 기능을 처리하는 서비스 클래스
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    /**
     * FileStorageService 생성자
     * @param uploadDir 파일 업로드 디렉토리 경로
     */
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * 파일을 저장합니다.
     * @param file 저장할 MultipartFile
     * @return 저장된 파일명
     * @throws IOException 파일 저장 중 오류 발생 시
     */
    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * 파일 저장 위치를 반환합니다.
     * @return 파일 저장 위치 경로
     */
    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }
}
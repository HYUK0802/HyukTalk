package com.hyuk.side.services;

import com.hyuk.side.compents.FileStorageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path profileImageLocation;
    private final Path chatImageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        // 프로필 사진 저장 경로
        this.profileImageLocation = Paths.get(fileStorageProperties.getUploadDir(), "profile-images")
                .toAbsolutePath()
                .normalize();

        // 채팅 이미지 저장 경로
        this.chatImageLocation = Paths.get(fileStorageProperties.getUploadDir(), "chat-images")
                .toAbsolutePath()
                .normalize();

        try {
            // 디렉토리 생성
            Files.createDirectories(this.profileImageLocation);
            Files.createDirectories(this.chatImageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directories for file storage.", e);
        }
    }

    // 프로필 이미지 저장
    public String storeProfileImage(MultipartFile file) {
        return storeFile(file, profileImageLocation, "/uploads/profile-images/");
    }

    // 채팅 이미지 저장
    public String storeChatImage(MultipartFile file) {
        return storeFile(file, chatImageLocation, "/uploads/chat-images/");
    }

    // 파일 저장 공통 로직
    private String storeFile(MultipartFile file, Path targetLocation, String urlPrefix) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // 파일 이름 검증
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid path sequence in file name: " + fileName);
            }

            // 저장 위치 및 파일 저장
            Path targetPath = targetLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 가상 경로 반환
            return urlPrefix + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Please try again!", e);
        }
    }


}


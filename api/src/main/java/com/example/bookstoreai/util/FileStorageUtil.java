package com.example.bookstoreai.util;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageUtil {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path bookImagesPath;
    private Path bookFilesPath;

    @PostConstruct
    public void init() {
        bookImagesPath = Paths.get(uploadDir, "books");
        bookFilesPath = Paths.get(uploadDir, "files");
        try {
            Files.createDirectories(bookImagesPath);
            Files.createDirectories(bookFilesPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear directorio de uploads", e);
        }
    }

    public String saveBookImage(MultipartFile file, String slug) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String fileName = slug + extension;
        Path targetPath = bookImagesPath.resolve(fileName);

        // Compress and resize to max 800x800 maintaining aspect ratio
        Thumbnails.of(file.getInputStream())
                .size(800, 800)
                .keepAspectRatio(true)
                .outputQuality(0.8)
                .toFile(targetPath.toFile());

        return "/uploads/books/" + fileName;
    }

    public void deleteBookImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            Files.deleteIfExists(bookImagesPath.resolve(fileName));
        } catch (IOException e) {
            // Log but don't fail
        }
    }

    public String saveBookFile(MultipartFile file, String slug) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String fileName = slug + extension;
        Path targetPath = bookFilesPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/files/" + fileName;
    }

    public Path getBookFilePath(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        return bookFilesPath.resolve(fileName);
    }

    public void deleteBookFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Files.deleteIfExists(bookFilesPath.resolve(fileName));
        } catch (IOException e) {
            // Log but don't fail
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf("."));
    }
}

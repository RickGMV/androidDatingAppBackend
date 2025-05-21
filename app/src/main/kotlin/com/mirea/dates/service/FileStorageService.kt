package com.mirea.dates.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Service
class FileStorageService(
    @Value("\${file.upload-dir}") private val uploadDir: String
) {
    init {
        Files.createDirectories(Paths.get(uploadDir))
    }

    fun storeFile(file: MultipartFile): String {
        require(file.contentType?.startsWith("image/") == true) {
            "Only images are allowed"
        }
        require(file.size < 5_000_000) {
            "Max file size 5MB exceeded"
        }

        val extension = file.originalFilename?.substringAfterLast('.')
        val allowedExtensions = setOf("jpg", "jpeg", "png")

        require(extension in allowedExtensions) {
            "Unsupported file format. Allowed: ${allowedExtensions.joinToString()}"
        }
        try {
            val fileName = "${UUID.randomUUID()}_${file.originalFilename}"
            val filePath = Paths.get(uploadDir, fileName)

            file.transferTo(filePath.toFile())
            return "/uploads/$fileName"
        } catch (e: Exception) {
            throw RuntimeException("Failed to store file: ${e.message}", e)
        }
    }
}
package cn.fbi.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/postCover")
public class CoverController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Value("${file.upload-dir}")
    private String uploadDir;
    @PostMapping("/upload/{postId}")
    public String uploadCover(@PathVariable String postId, @RequestParam("file") MultipartFile file) {
        // 校验文件类型
        if (!file.getContentType().startsWith("image")) {
            return "文件格式不支持";
        }

        // 生成文件名
        String fileName = postId + "Cover.png";

        try {
            // 检查并创建目录
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // 文件保存路径
            Path filePath = Paths.get(uploadDir + fileName);

            // 将文件保存到指定位置
            Files.write(filePath, file.getBytes());

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/postCover/download/")
                    .path(fileName)
                    .toUriString();

            logger.info("头像上传成功：" + fileName);
            return "头像上传成功：" + fileName;
        } catch (IOException ex) {
            logger.error("文件上传失败：" + ex.getMessage());
            return "文件上传失败：" + ex.getMessage();
        }
    }

    @GetMapping("/download/{userId}")
    public byte[] downloadAvatar(@PathVariable String postId) throws IOException {
        String fileName = postId + "Cover.png";
        Path filePath = Paths.get(uploadDir + fileName);
        return Files.readAllBytes(filePath);
    }
}

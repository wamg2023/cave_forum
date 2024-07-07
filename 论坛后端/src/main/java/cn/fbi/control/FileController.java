package cn.fbi.control;


import cn.fbi.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * Author:王顺康
 *评论控制器，处理用户头像上传与下载相关的HTTP请求
 * */
@RestController
@RequestMapping("/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    // 文件上传目录
    @Value("${file.upload-dir}")
    private String uploadDir;
    // 处理头像上传
    @PostMapping("/upload")
    public Result uploadAvatar(@PathVariable("userId") String userId, @RequestParam("avatar_file") MultipartFile file) {
        // 构建文件名，以 userId + "Avatar.png" 形式命名
        String fileName = userId + "Avatar.png";
        // 构建上传路径
        File dest = new File(uploadDir + File.separator + fileName);
        try {
            // 如果存在同名文件，则先删除
            if (dest.exists()) {
                dest.delete();
            }
            // 将文件写入指定路径
            file.transferTo(dest);
            // 返回上传成功的响应，包含文件下载链接
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/download/avatar/")
                    .path(fileName)
                    .toUriString();
            logger.info("头像上传成功，头像文件名"+fileName);
            return Result.Success("头像上传成功，头像文件名",fileName);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("系统异常，头像上传失败，请重试！");
            return Result.Error("头像上传失败，请重试！");
        }
    }

    // 处理头像文件下载
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadAvatar(@PathVariable String fileName, HttpServletRequest request) {
        // 构建文件路径
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Resource resource;
        try {
            // 加载文件资源
            resource = new UrlResource(filePath.toUri());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        // 确保文件存在并可读
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        // 获取文件的MIME类型
        String contentType = "application/octet-stream";

        // 处理IE浏览器文件名乱码问题
        String header = request.getHeader("User-Agent");
        if (header.contains("MSIE") || header.contains("Trident") || header.contains("Edge")) {
            try {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
                logger.info("文件获取成功！");
            } catch (java.io.UnsupportedEncodingException e) {
                logger.info("文件获取失败！");
                e.printStackTrace();
            }
        }
        // 构建下载响应
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}


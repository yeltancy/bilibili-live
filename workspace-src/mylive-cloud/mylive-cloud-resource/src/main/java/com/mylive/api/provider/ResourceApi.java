package com.mylive.api.provider;

import com.mylive.annotation.GlobalInterceptor;
import com.mylive.controller.FileController;
import com.mylive.entity.constants.Constants;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/file")
public class ResourceApi {

    @Resource
    private FileController fileController;

    @ApiOperation("上传图片封面")
    @RequestMapping("/uploadImage")
    @GlobalInterceptor(checkLogin = true)
    public String uploadImage(@NotNull MultipartFile file, @NotNull Boolean createThumbnail) throws IOException {
        return fileController.uploadImageInner(file, createThumbnail);
    }

    @ApiOperation("获取资源信息")
    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse response, @NotEmpty String sourceName) throws IOException {
        fileController.getResource(response, sourceName);
    }

    @ApiOperation("流获取文件视频m3u8")
    @RequestMapping("/videoResource/{fileId}")
    public void videoResource(HttpServletResponse response, @PathVariable @NotEmpty String fileId) {
        fileController.getVideoResource(response, fileId);
    }

    @ApiOperation("流获取文件视频TS信息")
    @RequestMapping("/videoResource/{fileId}/{ts}")
    public void videoResourceTs(HttpServletResponse response, @PathVariable @NotEmpty String fileId, @PathVariable @NotEmpty String ts) {
        fileController.getVideoResourceTs(response, fileId, ts);
    }
}

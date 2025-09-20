package com.mylive.controller;

import com.mylive.api.consumer.ResourceClient;
import com.mylive.entity.config.AppConfig;
import com.mylive.entity.constants.Constants;
import com.mylive.entity.enums.DateTimePatternEnum;
import com.mylive.entity.enums.ResponseCodeEnum;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.exception.BusinessException;
import com.mylive.utils.DateUtil;
import com.mylive.utils.FFmpegUtils;
import com.mylive.utils.StringTools;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

@RestController
@RequestMapping("/file")
@Validated
@Slf4j
public class FileController extends ABaseController {

    @Resource
    private ResourceClient resourceClient;


    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(@NotNull MultipartFile file,
                                  @NotNull Boolean createThumbnail) throws IOException {
        return getSuccessResponseVO(resourceClient.uploadImage(file, createThumbnail));
    }

    @RequestMapping("/getResource")
    public void getResource(HttpServletResponse servletResponse, @NotNull String sourceName) {
        Response response = resourceClient.getResource(sourceName);
        convertFileReponse2Stream(servletResponse, response);
    }

    @RequestMapping("/videoResource/{fileId}")
    public void getVideoResource(HttpServletResponse servletResponse, @PathVariable @NotNull String fileId) {
        convertFileReponse2Stream(servletResponse, resourceClient.videoResource(fileId));
    }

    @RequestMapping("/videoResource/{fileId}/{ts}")
    public void getVideoResourceTs(HttpServletResponse servletResponse, @PathVariable @NotNull String fileId, @PathVariable @NotNull String ts) {
        convertFileReponse2Stream(servletResponse, resourceClient.getVideoResourceTs(fileId, ts));
    }
}

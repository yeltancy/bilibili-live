package com.mylive.handler;

import com.mylive.entity.enums.ResponseCodeEnum;
import com.mylive.entity.vo.ResponseVO;
import com.mylive.exception.BusinessException;
import com.mylive.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@Order(-1)
public class GatewayExceptionHandler implements WebExceptionHandler {
    protected static final String STATUC_ERROR = "error";
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ResponseVO responseVO = getResponse(ex);

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtils.convertObj2Json(responseVO).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

    private ResponseVO getResponse(Throwable ex) {
        ResponseVO responseVO = new ResponseVO();
        if(ex instanceof ResponseStatusException){
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            if(HttpStatus.NOT_FOUND == responseStatusException.getStatus()) {
                responseVO.setCode(ResponseCodeEnum.CODE_404.getCode());
                responseVO.setInfo(ResponseCodeEnum.CODE_404.getMsg());
                responseVO.setStatus(STATUC_ERROR);
                return responseVO;
            } else if (HttpStatus.SERVICE_UNAVAILABLE == responseStatusException.getStatus()) {
                responseVO.setCode(ResponseCodeEnum.CODE_503.getCode());
                responseVO.setInfo(ResponseCodeEnum.CODE_503.getMsg());
                return responseVO;
            } else  {
                responseVO.setCode(responseStatusException.getStatus().value());
                responseVO.setInfo(ResponseCodeEnum.CODE_500.getMsg());
                return responseVO;
            }
        } else if (ex instanceof BusinessException) {
            BusinessException exception = (BusinessException) ex;
            responseVO.setCode(exception.getCode());
            responseVO.setInfo(exception.getMessage());
            return responseVO;
        }
        responseVO.setCode(ResponseCodeEnum.CODE_500.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        return responseVO;
    }
}

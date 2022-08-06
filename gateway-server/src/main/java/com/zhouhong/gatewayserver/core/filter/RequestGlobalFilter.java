package com.zhouhong.gatewayserver.core.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhouhong.gatewayserver.core.consts.ReleaseConstant;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * 全局请求转换
 **/
@Log4j2
@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    ServerCodecConfigurer codecConfigurer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (Objects.equals(exchange.getRequest().getMethod(), HttpMethod.POST)) {

            // 表单传输
            MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
            if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType)) {
                return returnMono(chain, exchange);
            }

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            //放开不进行安全过滤的接口
            boolean checkSign = true;
            for (String notAuthResource : ReleaseConstant.NONE_SECURITY_URL_PATTERNS) {
                AntPathMatcher antPathMatcher = new AntPathMatcher();
                if (antPathMatcher.match(notAuthResource, path)) {
                    checkSign = false;
                }
            }

            ServerRequest serverRequest = ServerRequest.create(exchange, codecConfigurer.getReaders());

            Mono modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
                //因为约定了终端传参的格式，所以只考虑json的情况，如果是表单传参，请自行发挥
                if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                    JSONObject jsonObject = JSON.parseObject(body);
                    String newBody = null;
                    try {
                        // 如果是1.0版本的请求（body经过base64转码），要将body解码
                        if (jsonObject.containsKey("sign") || jsonObject.containsKey("object")) {
                            newBody = verifySignature(jsonObject.getString("object"));
                        } else {
                            newBody = body;
                        }
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                    log.info("请求：" + path + " " + body);
                    return Mono.just(newBody);
                }
                return Mono.just(body);
            });
            BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove("Content-Length");
            // 请求中添加TOKEN用户信息
            addTokenInfo(headers, serverRequest);
            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
            Mono mono = bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                ServerHttpRequest decorator = this.decorate(exchange, headers, outputMessage);
                return returnMono(chain, exchange.mutate().request(decorator).build());
            }));
            return mono;
        } else {
            //GET 验签
            return returnMono(chain, exchange);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }


    private Mono<Void> returnMono(GatewayFilterChain chain, ServerWebExchange exchange) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
        }));
    }

    private String verifySignature(String paramStr) {
        return new String(Base64Utils.decodeFromString(paramStr));
    }

    private Mono processError(String message) {
        log.error(message);
        return Mono.error(new Exception(message));
    }

    /**
     * 网关抛异常
     */
    @NotNull
    private Mono getVoidMono(ServerWebExchange serverWebExchange, ResponseData body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(headers);
                if (contentLength > 0L) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set("Transfer-Encoding", "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    private void addTokenInfo(HttpHeaders headers, ServerRequest request) {
        return;
    }
}

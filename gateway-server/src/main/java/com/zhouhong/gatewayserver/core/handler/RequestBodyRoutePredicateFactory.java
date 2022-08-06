package com.zhouhong.gatewayserver.core.handler;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * This predicate is BETA and may be subject to change in a future release.
 */
@Component
@Order(1)
public class RequestBodyRoutePredicateFactory
        extends AbstractRoutePredicateFactory<RequestBodyRoutePredicateFactory.Config> {
    protected static final Log LOGGER = LogFactory.getLog(RequestBodyRoutePredicateFactory.class);
    private final List<HttpMessageReader<?>> messageReaders;

    public RequestBodyRoutePredicateFactory() {
        super(Config.class);
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();
    }

    public RequestBodyRoutePredicateFactory(List<HttpMessageReader<?>> messageReaders) {
        super(Config.class);
        this.messageReaders = messageReaders;
    }
    public static final String REQUEST_BODY_ATTR = "requestBodyAttr";


    @Override
    public AsyncPredicate<ServerWebExchange> applyAsync(Config config) {
        return exchange -> {
            if (!"POST".equals(exchange.getRequest().getMethodValue())&&!"PUT".equals(exchange.getRequest().getMethodValue())) {
                return Mono.just(true);
            }
            Object cachedBody = exchange.getAttribute(REQUEST_BODY_ATTR);
            if (cachedBody != null) {
                try {
                    return Mono.just(true);
                } catch (ClassCastException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Predicate test failed because class in predicate does not match the cached body object",
                                e);
                    }
                }
                return Mono.just(true);
            } else {
                return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange, (serverHttpRequest) -> ServerRequest.create(exchange.mutate().request(serverHttpRequest).build(), this.messageReaders).bodyToMono(String.class).defaultIfEmpty("").doOnNext((objectValue) -> {
                    if(StringUtils.isBlank(objectValue)){
                        exchange.getAttributes().put(REQUEST_BODY_ATTR, JSON.toJSONString(exchange.getRequest().getQueryParams()));
                    }else {
                        exchange.getAttributes().put(REQUEST_BODY_ATTR, objectValue);
                    }
                }).map((objectValue) -> true));

            }
        };
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        throw new UnsupportedOperationException(
                "ReadBodyPredicateFactory is only async.");
    }

    public static class Config {
        private List<String> sources = new ArrayList<>();

        public List<String> getSources() {
            return sources;
        }

        public Config setSources(List<String> sources) {
            this.sources = sources;
            return this;
        }

        public Config setSources(String... sources) {
            this.sources = Arrays.asList(sources);
            return this;
        }
    }
}

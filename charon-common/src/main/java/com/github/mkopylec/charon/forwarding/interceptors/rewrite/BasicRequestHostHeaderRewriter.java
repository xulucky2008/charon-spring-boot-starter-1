package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.URI;
import java.util.function.Consumer;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_HOST_HEADER_REWRITER;
import static org.springframework.http.HttpHeaders.HOST;

abstract class BasicRequestHostHeaderRewriter implements Ordered, Valid {

    private Logger log;

    BasicRequestHostHeaderRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return REQUEST_HOST_HEADER_REWRITER.getOrder();
    }

    void rewriteHeaders(HttpHeaders headers, URI uri, Consumer<HttpHeaders> headersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        rewrittenHeaders.set(HOST, uri.getAuthority());
        headersSetter.accept(rewrittenHeaders);
        log.debug("Request headers rewritten from {} to {}", headers, rewrittenHeaders);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request 'Host' header rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request 'Host' header rewriting for '{}' request mapping", mappingName);
    }
}
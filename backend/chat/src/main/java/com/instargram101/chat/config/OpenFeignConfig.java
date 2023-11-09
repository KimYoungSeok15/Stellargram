package com.instargram101.chat.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.instargram101.chat")
public class OpenFeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
//            requestTemplate.header("Accept", "application/json");
            if (ArrayUtils.isEmpty(requestTemplate.body()) && !isGetOrDelete(requestTemplate)) {
                requestTemplate.body("{}");
            }
        }; //Post, Put 요청시 Body가 비면 411에러가 발생함.
    }

    private boolean isGetOrDelete(RequestTemplate requestTemplate) {
        return Request.HttpMethod.GET.name().equals(requestTemplate.method())
                || Request.HttpMethod.DELETE.name().equals(requestTemplate.method());
    }

}

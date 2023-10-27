package com.instargram101.global.util.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "memberFeign", url = "http://k9a101.p.ssafy.io:8000/member", configuration = MemberFeignClient.class)
public interface MemberFeignClient {

}

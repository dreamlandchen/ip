package com.dreamland.iplocation.controller;

import com.dreamland.iplocation.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zora
 * @Date 16:06 2021/03/24
 * @Description:
 * @Modified By
 */
@RestController
@RequestMapping("/ip")
public class IpAddressAction {
    private static final Logger logger = LoggerFactory.getLogger(IpAddressAction.class);

    @RequestMapping(method = RequestMethod.GET, value = "/{ip}")
    public String getIpAddress(@PathVariable(name = "ip") String ip) {
        if(!IpUtil.validIP(ip)){
            return "无效IP";
        }
        try {
            return IpUtil.getCityInfo(ip);
        } catch (Exception e) {
            logger.error("查询IP地址出错：{}", e.getMessage());
        }
        return "查询IP地址出错";
    }
}

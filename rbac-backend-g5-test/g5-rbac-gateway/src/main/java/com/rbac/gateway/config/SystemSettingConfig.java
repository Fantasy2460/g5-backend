package com.rbac.gateway.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 系统的配置信息
 *  kp: 从nacos中获取相关的系统配置，
 * @author Lzzh
 * @version 1.0
 */
@Slf4j
@Component
@Data
@ConfigurationProperties("access")
public class SystemSettingConfig {

    private List<String> blackList;
    private List<String> whiteList;

    private Boolean degradation;
    @PostConstruct
    void  test() {
        log.error("blackList:");
        for (String s : blackList) {
            log.info(s);
        }
        log.error("whiteList:");
        for (String s : whiteList) {
            log.info(s);
        }
        log.error("degradation");
        log.info(String.valueOf(degradation));
    }

}

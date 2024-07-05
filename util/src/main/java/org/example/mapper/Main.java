package org.example.mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        UserVo userVo = MapStructUtil.INSTANCE.copy(new User().setUsername("lpl"));
        log.info(userVo.toString());
    }
}

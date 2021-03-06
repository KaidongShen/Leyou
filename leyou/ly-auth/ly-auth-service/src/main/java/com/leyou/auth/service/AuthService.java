package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    public String login(String username, String password) {
        try{
        // 校验用户名和密码
        User user = userClient.queryUserByUsernameAndPassword(username, password);
        if (user == null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), prop.getPrivateKey(),30);

        return token;
    } catch (Exception e){
            log.error("[授权中心] 用户名或密码有误, 用户名称:{}",username,e);
            // TODO 这里的exception不知道为什么不对. 再检查.
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

    }
}

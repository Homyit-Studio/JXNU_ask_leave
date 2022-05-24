package cn.homyit.onlineLeaveSystem.handler;

import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.util.WebUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)  {
//        if (authException.getClass())
        Result<Object> result = Result.error(ExceptionCodeEnum.TOKEN_EXPIRED);
        String jsonString = JSON.toJSONString(result);
        WebUtils.renderString(response,jsonString);
    }
}
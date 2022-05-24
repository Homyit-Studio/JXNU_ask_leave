package cn.homyit.onlineLeaveSystem.handler;

import cn.homyit.onlineLeaveSystem.entity.VO.Result;
import cn.homyit.onlineLeaveSystem.exception.ExceptionCodeEnum;
import cn.homyit.onlineLeaveSystem.util.WebUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        Result<Object> result = Result.error(ExceptionCodeEnum.FORBIDDEN);
        String jsonString = JSON.toJSONString(result);
        WebUtils.renderString(response,jsonString);
    }
}

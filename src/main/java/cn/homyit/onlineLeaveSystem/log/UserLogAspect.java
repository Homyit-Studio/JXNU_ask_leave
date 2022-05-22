package cn.homyit.onlineLeaveSystem.log;

import cn.homyit.onlineLeaveSystem.entity.DO.LoginUser;
import cn.homyit.onlineLeaveSystem.entity.DO.SysStudentUser;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserInfoLogDTO;
import cn.homyit.onlineLeaveSystem.entity.DTO.UserLogDTO;
import cn.homyit.onlineLeaveSystem.service.UserLogService;
import cn.homyit.onlineLeaveSystem.util.MyBeanUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志注解切面实现
 */
@Slf4j
@Aspect
public class UserLogAspect {
	// 这次不用RequestContextHolder了，改成直接注入
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    UserLogService userLogService;


    @Pointcut("@annotation(cn.homyit.onlineLeaveSystem.log.UserLog)")
    public void pointcut() {
    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint point) {
        saveSysUserLog(point);
    }

    private void saveSysUserLog(JoinPoint point) {
        // 获取当前登录用户
        UserInfoLogDTO userInfoDTO = getUserInfoDTO();

        // 目标方法、以及方法上的@UserLog注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        UserLog userLogAnnotation = method.getAnnotation(UserLog.class);
        if (userLogAnnotation == null) {
            return;
        }

        // 收集相关信息并保存
        UserLogDTO userLogDTO = new UserLogDTO();
        userLogDTO.setModuleCode(userLogAnnotation.module().getModuleCode());
        //收集内容
        userLogDTO.setContent(getContentJson(point));

        userLogDTO.setTitle(userLogAnnotation.title());
        userLogDTO.setStudentNumber(userInfoDTO.getStudentNumber());
        userLogDTO.setUsername(userInfoDTO.getUsername());
        userLogDTO.setOperateTime(new Date());
        userLogDTO.setType(userLogAnnotation.type().getValue());

        //持久化日志
        userLogService.addSysLog(userLogDTO);
    }

    private UserInfoLogDTO getUserInfoDTO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysStudentUser user = loginUser.getUser();
        UserInfoLogDTO userInfoDTO = MyBeanUtils.copyBean(user, UserInfoLogDTO.class);
        return userInfoDTO;
    }

    private String getContentJson(JoinPoint point) {
        String requestType = request.getMethod();
        if ("GET".equals(requestType)) {
            // 如果是GET请求，直接返回QueryString（查询操作不进行日志记录，先留着吧）
            return request.getQueryString();
        }

        Object[] args = point.getArgs();
        Object[] arguments = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            // 只打印客户端传递的参数，排除Spring注入的参数，比如HttpServletRequest
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }

        try {
            return objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            log.error("UserLogAspect#getContentJson JsonProcessingException", e);
        }
        return "";
    }
}
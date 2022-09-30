package cn.homyit.onlineLeaveSystem.service;

import javax.servlet.http.HttpServletRequest;

public interface EmailService {
    String resetPasswordByEmail(String email, HttpServletRequest request);

}

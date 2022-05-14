package cn.homyit.onlineLeaveSystem.job;

import cn.homyit.onlineLeaveSystem.service.LeaveNoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 州牧
 * @description 简单实现假条的过期状态更新
 * @since 2022-05-13 09:25
 */
@Slf4j
@Component
public class UpdateNoteStatusScheduler {

    @Autowired
    private LeaveNoteService leaveNoteService;



    @Scheduled(cron = "1/59 * * * * ?")
    public void orderCheck(){
        leaveNoteService.checkExpired();
        log.info("检测失效假条"+new Date());
    }
}

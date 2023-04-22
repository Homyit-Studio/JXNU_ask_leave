package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserLogDTO {

    private Long studentNumber;

    private String username;

    private String moduleCode;

    private Integer type;

    private String title;

    private Long operatorId;

    private Date operateTime;

    private String content;

}
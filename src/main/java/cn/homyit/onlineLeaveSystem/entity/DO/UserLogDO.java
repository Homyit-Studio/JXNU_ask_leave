package cn.homyit.onlineLeaveSystem.entity.DO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user_log")
public class UserLogDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long studentNumber;

    private String username;

    private String moduleCode;

    private Integer type;

    private String title;


    private Date operateTime;

    private String content;

}
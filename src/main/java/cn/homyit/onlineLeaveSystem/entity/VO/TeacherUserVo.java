package cn.homyit.onlineLeaveSystem.entity.VO;

import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import lombok.Data;

/**
 * @author 州牧
 * @description
 * @since 2022-05-06 21:36
 */
@Data
public class TeacherUserVo {
    private Long id;
    private LevelEnum role;
    private String username;
    private Long studentNumber;
    private SexEnum sex;
    private String phoneNumber;

}

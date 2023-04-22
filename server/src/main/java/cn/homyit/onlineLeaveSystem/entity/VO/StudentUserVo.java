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
public class StudentUserVo {
    private Long id;
    private Long classId;
    private LevelEnum role;
    private String username;
    private String email;
    private String idNumber;
    private String nation;
    private Long studentNumber;
    private SexEnum sex;
    private String nativePlace;
    private String homeAddress;
    private String majorAndClass;
    private String phoneNumber;
    private String parentNumber;
    private String buildingNumber;
    private String dormitoryNumber;
}

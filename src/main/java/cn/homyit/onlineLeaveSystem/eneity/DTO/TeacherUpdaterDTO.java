package cn.homyit.onlineLeaveSystem.eneity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import lombok.Data;

/**
 * @author 州牧
 * @description
 * @since 2022-05-06 21:36
 */
@Data
public class TeacherUpdaterDTO {
    private Long id;
    private Long classId;
    private Long gradeId;
    private String password;
    private LevelEnum role;
    private String username;
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

package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 州牧
 * @description
 * @since 2022-05-06 21:36
 */
@Data
public class TeacherUpdaterDTO {
    @NotNull(message = "id不能为空")
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

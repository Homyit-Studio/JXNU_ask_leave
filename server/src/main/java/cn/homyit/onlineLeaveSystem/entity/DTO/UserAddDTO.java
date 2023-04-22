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
public class UserAddDTO {

    private Long classId;
    private Long gradeId;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "角色不能为空")
    private LevelEnum role;
    @NotNull(message = "姓名不能为空")
    private String username;
    private String idNumber;
    private String nation;
    @NotNull(message = "账号不能为空")
    private Long studentNumber;
    private SexEnum sex;
    private String nativePlace;
    private String homeAddress;
    private String majorAndClass;
    @NotNull(message = "手机号不能为空")
    private String phoneNumber;
    private String parentNumber;
    private String buildingNumber;
    private String dormitoryNumber;
}

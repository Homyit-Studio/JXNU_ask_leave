package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 10:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {
    @NotNull(message = "原密码不能为空")
    private String oldPassword;
    @NotNull(message = "新密码不能为空")
    private String newPassword;

}

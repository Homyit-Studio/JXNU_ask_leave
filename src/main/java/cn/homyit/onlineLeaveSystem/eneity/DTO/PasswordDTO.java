package cn.homyit.onlineLeaveSystem.eneity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 10:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {

    private String oldPassword;

    private String newPassword;

}

package cn.homyit.onlineLeaveSystem.eneity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 09:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long studentNumber;
    private String username;
    private String majorAndClass;
    //辅导员
    private String highUser;
}

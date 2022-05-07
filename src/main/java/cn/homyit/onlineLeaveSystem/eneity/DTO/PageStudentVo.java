package cn.homyit.onlineLeaveSystem.eneity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 17:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageStudentVo {
    private Integer pageNo;
    private Integer pageSize;
    private Integer classId;
}

package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 17:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageStudentDTO {
    @NotNull(message = "页面号不能为空")
    private Integer pageNo;
    @NotNull(message = "页面容量不能为空")
    private Integer pageSize;
    @NotNull(message = "班级不能为空")
    private Integer classId;
}

package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 15:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectNotePageDTO {
    private Integer pageNo;
    private Integer pageSize;
//    private CompleteEnum completeEnum;
    @NotNull(message = "假条状态不能为空")
    private ExamineEnum examineEnum;
    private Long studentNumber;
    private Long gradeId;
}

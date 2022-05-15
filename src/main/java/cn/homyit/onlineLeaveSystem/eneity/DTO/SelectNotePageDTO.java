package cn.homyit.onlineLeaveSystem.eneity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private ExamineEnum examineEnum;
    private Long studentNumber;
    private Long gradeId;
}

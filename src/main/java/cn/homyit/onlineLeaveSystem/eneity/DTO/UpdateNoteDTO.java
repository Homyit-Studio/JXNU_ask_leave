package cn.homyit.onlineLeaveSystem.eneity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OpinionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-05 16:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteDTO {
    private Long id;
    private ExamineEnum examineEnum;
    private OpinionEnum opinionEnum;
    private LevelEnum levelEnum;
    private String instructorOpinion;
    private String instituteOpinion;
    private String deanOpinion;
}

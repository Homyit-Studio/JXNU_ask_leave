package cn.homyit.onlineLeaveSystem.eneity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
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
    private String leaderNumber;
    private ExamineEnum examineEnum;
    private OpinionEnum opinionEnum;
    private LevelEnum levelEnum;
    private LeaveEnum depart;
    private String instructorOpinion;
    private String secretaryOpinion;
    private String deanOpinion;

}

package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OpinionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 州牧
 * @description
 * @since 2022-05-05 16:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNoteDTO {
    @NotNull(message = "假条id不能为空")
    private Long id;//假条id
    private String leaderNumber;//需要传递的领导账号 1,2
    @NotNull(message = "审核意见不能为空")
    private OpinionEnum opinionEnum;//同意或拒绝 YES NO
    @NotNull(message = "假条等级不能为空")
    private LevelEnum levelEnum;//假条等级
    private String instructorOpinion;//班主任意见
    private String secretaryOpinion;//负责人意见
    private String deanOpinion;//院长意见

}

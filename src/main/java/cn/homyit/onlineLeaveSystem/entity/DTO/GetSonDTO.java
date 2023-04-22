package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.Data;

/**
 * @author 州牧
 * @description
 * @since 2022-11-22 16:35
 */
@Data
public class GetSonDTO extends PageDTO{
    private Long rootCommentId;
    private Long discussionId;
    private Integer sonOpinion;
}

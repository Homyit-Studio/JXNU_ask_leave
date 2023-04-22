package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.Data;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 18:17
 */
@Data
public class CommentPageDTO extends PageDTO{
    private Long discussionId;
    private Integer sonPageNo;
    private Integer sonPageSize;
    private Integer parentOpinion;
    private Integer sonOpinion;
}

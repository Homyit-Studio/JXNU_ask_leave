package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import lombok.Data;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 16:58
 */
@Data
public class DiscussionDTO extends PageDTO{
    private String title;
    private Integer space;
}

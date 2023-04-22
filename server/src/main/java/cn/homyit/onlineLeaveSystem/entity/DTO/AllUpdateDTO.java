package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.OpinionEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * @author 州牧
 * @description
 * @since 2022-10-06 17:01
 */
@Data
public class AllUpdateDTO {

    private Long id;
    private LevelEnum levelEnum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    private LeaveEnum depart;
    private OpinionEnum opinionEnum;
    private String instructorOpinion;
    private String secretaryOpinion;
    private String deanOpinion;

    private String leaderNumber;
}

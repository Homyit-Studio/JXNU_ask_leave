package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 州牧
 * @description 根据学号或者姓名查找
 * @since 2022-10-06 16:12
 */
@Data
public class FindNotesDTO {
    private Integer pageNo;
    private Integer pageSize;

    private Long studentNumber;
    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private ExamineEnum examineEnum;
}

package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.service.impl.LeaveNoteServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 州牧
 * @description 报表展示
 * @since 2022-05-22 11:31
 */
@Data
@Getter
public class TableTimeDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
//    @NotNull(message = "请选择年级",groups = .class)
    private Long gradeId;
}

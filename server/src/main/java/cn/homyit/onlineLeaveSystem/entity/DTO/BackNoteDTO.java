package cn.homyit.onlineLeaveSystem.entity.DTO;

import cn.homyit.onlineLeaveSystem.myEnum.BackEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 州牧
 * @description
 * @since 2022-05-07 22:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackNoteDTO {

    @NotNull(message = "假条id不能为空")
    private Long id;
    private LeaveEnum depart;
    private BackEnum back;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date departTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date backTime;
    private String backWay;
    private String departWay;
}

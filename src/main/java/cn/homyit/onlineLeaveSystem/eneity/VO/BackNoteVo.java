package cn.homyit.onlineLeaveSystem.eneity.VO;

import cn.homyit.onlineLeaveSystem.myEnum.BackEnum;
import cn.homyit.onlineLeaveSystem.myEnum.BackStatusEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 州牧
 * @description
 * @since 2022-05-08 16:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackNoteVo {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date departTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date backTime;
    private String departWay;
    private LeaveEnum depart;
    private String backWay;
    private BackEnum back;
    private BackStatusEnum status;
}

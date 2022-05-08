package cn.homyit.onlineLeaveSystem.eneity.DO;

import cn.homyit.onlineLeaveSystem.myEnum.BackEnum;
import cn.homyit.onlineLeaveSystem.myEnum.BackStatusEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackNote  {

  private Long id;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date departTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date backTime;
  private String departWay;
  private String backWay;
  private LeaveEnum depart;
  private BackEnum back;

  private BackStatusEnum status;
  @TableLogic
  private Integer deleted;


}

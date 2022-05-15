package cn.homyit.onlineLeaveSystem.eneity.DO;


import cn.homyit.onlineLeaveSystem.myEnum.BackStatusEnum;
import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 渐入佳境
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveNote {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long studentNumber;
  private String majorAndClass;
  private String username;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date startTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date endTime;

  private Long gradeId;
  private LeaveEnum depart;

  private String destination;
  private String dormitoryNumber;
  private String way;
  private String phoneNumber;
  private String reason;
  private String instructorOpinion;
  private String secretaryOpinion;
  private String deanOpinion;
  @TableLogic
  private Integer deleted;

  private ExamineEnum examine;

  private LevelEnum level;

  private BackStatusEnum status;

  private String days;

  private String leaderNumber;




}

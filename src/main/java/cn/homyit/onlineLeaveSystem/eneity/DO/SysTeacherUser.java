package cn.homyit.onlineLeaveSystem.eneity.DO;



import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_teacher_user")
public class SysTeacherUser implements Serializable {
  private static final long serialVersionUID = -40356785423868312L;

  @TableId(type = IdType.AUTO)
  private Long id;
  private Long studentNumber;
  private String password;
  private String phoneNumber;
  private String username;

  private SexEnum sex;

  private String highUser;
  private String majorAndClass;

  private Date createTime;
  private Date updateTime;
  @TableLogic
  private Integer deleted;




}

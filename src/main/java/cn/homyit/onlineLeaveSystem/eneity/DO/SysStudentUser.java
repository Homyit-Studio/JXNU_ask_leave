package cn.homyit.onlineLeaveSystem.eneity.DO;



import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.SexEnum;
import com.alibaba.excel.annotation.ExcelProperty;
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
@TableName(value = "sys_student_user")
public class SysStudentUser implements Serializable {
  private static final long serialVersionUID = -40356785423868312L;

  @TableId(type = IdType.AUTO)
  private Long id;
  private Long classId;
  private LevelEnum role;
  private String highUser;
  private String username;
  private String idNumber;
  private String nation;
  private Long studentNumber;
  private SexEnum sex;
  private String nativePlace;
  private String homeAddress;
  private String majorAndClass;
  private String phoneNumber;
  private String parentNumber;
  private String buildingNumber;
  private String dormitoryNumber;
  private Long gradeId;
  private String password;
  private Date createTime;
  private Date updateTime;
  @TableLogic
  private Integer deleted;





}

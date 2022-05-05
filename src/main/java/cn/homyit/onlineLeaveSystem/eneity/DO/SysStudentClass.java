package cn.homyit.onlineLeaveSystem.eneity.DO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysStudentClass {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long classId;
  private Long studentNumber;
  private String username;
  private Long deleted;

}

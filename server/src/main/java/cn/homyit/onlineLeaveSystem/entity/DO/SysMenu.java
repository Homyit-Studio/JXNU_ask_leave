package cn.homyit.onlineLeaveSystem.entity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 渐入佳境
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu {

  private Long id;
  private String menuName;
  private String perms;
  private Long deleted;
}

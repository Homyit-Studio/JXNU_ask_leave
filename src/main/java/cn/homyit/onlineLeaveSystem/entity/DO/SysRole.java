package cn.homyit.onlineLeaveSystem.entity.DO;


import lombok.Data;

@Data
public class SysRole {

  private Long id;
  private String name;
  private String havePerms;
  private Long deleted;
}

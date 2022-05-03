package cn.homyit.onlineLeaveSystem.eneity.DO;


import lombok.Data;

@Data
public class SysRole {

  private Long id;
  private String name;
  private String havePerms;
  private Long deleted;
}

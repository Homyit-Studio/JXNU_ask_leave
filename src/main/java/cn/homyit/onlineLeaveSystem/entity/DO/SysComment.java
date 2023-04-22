package cn.homyit.onlineLeaveSystem.entity.DO;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SysComment {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long discussionId;
  private Long userId;
  private String userName;
  private String comment;
  private Long replyUserId;
  private String replyUserName;
  private Long replyCommentId;
  private Long rootCommentId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;
}

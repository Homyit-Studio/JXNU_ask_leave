package cn.homyit.onlineLeaveSystem.entity.VO;


import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import cn.homyit.onlineLeaveSystem.myEnum.converter.ExamineConverter;
import cn.homyit.onlineLeaveSystem.myEnum.converter.LeaveConverter;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 渐入佳境
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class LeaveNoteVo implements Serializable {
  private static final long serialVersionUID = -40356785423688312L;

  @ExcelProperty(index = 0,value = "假条序号")
  private Long id;
  @ExcelProperty(index = 1,value = "学号")
  private Long studentNumber;
  @ExcelProperty(index = 2,value = "专业班级")
  private String majorAndClass;
  @ExcelProperty(index = 3,value = "姓名")
  private String username;
  @ExcelProperty(index = 4,value = "起始时间")
//  @DateTimeFormat("yyyy-MM-dd")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date startTime;
  @ExcelProperty(index = 5,value = "终止时间")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date endTime;
  @ExcelProperty(index = 6,value = "是否离校",converter = LeaveConverter.class)
  private LeaveEnum depart;
  @ExcelProperty(index = 7,value = "目的地")
  private String destination;
  @ExcelProperty(index = 8,value = "宿舍号")
  private String dormitoryNumber;
  @ExcelProperty(index = 9,value = "交通方式")
  private String way;
  @ExcelProperty(index = 10 ,value = "联系方式")
  private String phoneNumber;
  @ExcelProperty(index = 11,value = "请假理由")
  private String reason;
  @ExcelProperty(index = 12,value = "辅导员意见")
  private String instructorOpinion;
  @ExcelProperty(index = 13,value = "负责人意见")
  private String secretaryOpinion;
  @ExcelProperty(index = 14,value = "院长意见")
  private String deanOpinion;
  @ExcelProperty(index = 15,value = "假条状态",converter = ExamineConverter.class)
  private ExamineEnum examine;

//  @ExcelIgnore
//  private BackStatusEnum status;
  @ExcelIgnore
  private LevelEnum level;
//  @ExcelIgnore
  private String days;

  
}

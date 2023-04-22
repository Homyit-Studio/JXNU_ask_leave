package cn.homyit.onlineLeaveSystem.entity.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author 州牧
 * @description
 * @since 2022-05-05 20:12
 */


@Data
public class StudentExcelDTO {

    @ExcelProperty(index = 1)
    private String username;
    @ExcelProperty(index = 2)
    private String idNumber;
    @ExcelProperty(index = 3)
    private String nation;
    @ExcelProperty(index = 4)
    private Long studentNumber;
    @ExcelProperty(index = 5)
    private String sex;
    @ExcelProperty(index = 6)
    private String nativePlace;
    @ExcelProperty(index = 7)
    private String homeAddress;
    @ExcelProperty(index = 8)
    private String majorAndClass;
    @ExcelProperty(index = 9)
    private String phoneNumber;
    @ExcelProperty(index = 10)
    private String parentNumber;
    @ExcelProperty(index = 11)
    private String buildingNumber;
    @ExcelProperty(index = 12)
    private String dormitoryNumber;




}

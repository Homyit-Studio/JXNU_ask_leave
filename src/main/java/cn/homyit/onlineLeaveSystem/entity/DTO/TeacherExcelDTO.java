package cn.homyit.onlineLeaveSystem.entity.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-06 17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherExcelDTO {
    @ExcelProperty(index = 1)
    private String username;
    @ExcelProperty(index = 2)
    private Long studentNumber;
    @ExcelProperty(index = 3)
    private String sex;
    @ExcelProperty(index = 4)
    private String manageClass;
    @ExcelProperty(index = 5)
    private String phoneNumber;
    @ExcelProperty(index = 6)
    private String role;
}

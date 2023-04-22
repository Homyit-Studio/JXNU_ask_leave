package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-15 11:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO {
    private String phoneNumber;
    private String parentNumber;
    private String buildingNumber;
    private String dormitoryNumber;
    private String email;
}

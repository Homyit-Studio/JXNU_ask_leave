package cn.homyit.onlineLeaveSystem.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 17:34
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassInfoVO {
    private Long id;
    private String majorAndClass;
    private Long gradeId;
    private Integer capacity;
}

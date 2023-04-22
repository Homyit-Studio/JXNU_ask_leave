package cn.homyit.onlineLeaveSystem.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 州牧
 * @description
 * @since 2022-11-20 16:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {
    private Integer pageNo;
    private Integer pageSize;
}

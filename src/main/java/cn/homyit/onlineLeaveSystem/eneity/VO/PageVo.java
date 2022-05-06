package cn.homyit.onlineLeaveSystem.eneity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {
     private List<T> list;
     private Long total;//记录数量
     private Long endPage;//总共多少页
}

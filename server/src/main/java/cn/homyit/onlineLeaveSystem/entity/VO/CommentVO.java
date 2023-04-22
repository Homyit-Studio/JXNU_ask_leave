package cn.homyit.onlineLeaveSystem.entity.VO;

import cn.homyit.onlineLeaveSystem.entity.DO.SysComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


/**
 * @author 州牧
 * @description
 * @since 2022-11-20 17:12
 */
@Data
public class CommentVO<T> extends SysComment {
    private PageVo<CommentVO> son;
}

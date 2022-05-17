package cn.homyit.onlineLeaveSystem.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author 州牧
 * @description
 * @since 2022-05-17 17:57
 */
@Data
@Getter
public class DownloadNoteDTOForGet {
    private String startTime;
    private String endTime;

    private  Long studentNumber;
    private  Long gradeId;
    private  Long classId;
}

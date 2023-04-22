package cn.homyit.onlineLeaveSystem.myEnum.converter;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LeaveEnum;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author 州牧
 * @description
 * @since 2022-05-13 22:25
 */
public class LeaveConverter implements Converter<LeaveEnum> {


    @Override
    public WriteCellData<?> convertToExcelData(LeaveEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.toString());
//        return Converter.super.convertToExcelData(value.toString(), contentProperty, globalConfiguration);
    }

}


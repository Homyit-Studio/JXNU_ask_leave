package cn.homyit.onlineLeaveSystem.myEnum.converter;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author 州牧
 * @description 将枚举字段转换为文字，导出假条需要
 * @since 2022-05-13 21:58
 */
public class ExamineConverter implements Converter<ExamineEnum> {
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<ExamineEnum> context) throws Exception {
        return Converter.super.convertToExcelData(context);
    }

    @Override
    public WriteCellData<?> convertToExcelData(ExamineEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.toString());
//        return Converter.super.convertToExcelData(value.toString(), contentProperty, globalConfiguration);
    }

}

package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 11:07
 */
public enum ExamineEnum implements IEnum<Integer> {
    INSTRUCTOR(0,"辅导员审核中"),
    SECRETARY(1,"副党委书记审核中"),
    DEAN(2,"院长审核中"),
    SUCCESS(-1,"审核完成")
    ;


    ExamineEnum(int value, String desc){
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString(){
        return desc;
    }

    private final int value;
    private final String desc;
}

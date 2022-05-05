package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 11:07
 */
public enum ExamineEnum implements IEnum<Integer> {
    INSTRUCTOR(1,"辅导员审核中"),
    SECRETARY(2,"副党委书记审核中"),
    DEAN(3,"院长审核中")
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

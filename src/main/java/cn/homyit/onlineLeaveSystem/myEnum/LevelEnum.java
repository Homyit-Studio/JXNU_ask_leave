package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum LevelEnum implements IEnum<Integer> {
    STUDENT(0,"学生"),
    INSTRUCTOR(1,"辅导员"),
    SECRETARY(2,"副党委书记"),
    DEAN(3,"院长"),
    LOOK(4,"查看")
    ;


    LevelEnum(int value, String desc){
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

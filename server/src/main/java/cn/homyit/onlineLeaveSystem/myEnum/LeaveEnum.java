package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum LeaveEnum implements IEnum<Integer> {
    YES(0,"离校"),
    NO(1,"留校")
    ;


    LeaveEnum(int value, String desc){
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

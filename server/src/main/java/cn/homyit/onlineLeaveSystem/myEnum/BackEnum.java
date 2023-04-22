package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author 州牧
 * @description
 * @since 2022-05-08 17:04
 */

public enum BackEnum implements IEnum<Integer> {
    YES(0,"在校"),
    NO(1,"离校")
    ;


    BackEnum(int value, String desc){
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
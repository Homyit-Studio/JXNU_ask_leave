package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author 州牧
 * @description
 * @since 2022-05-08 18:02
 */

public enum BackStatusEnum implements IEnum<Integer> {
    SUCCESS(0,"成功"),
    FAILURE(1,"失败")
    ;


    BackStatusEnum(int value, String desc){
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

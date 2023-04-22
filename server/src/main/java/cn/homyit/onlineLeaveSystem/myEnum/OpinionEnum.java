package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum OpinionEnum implements IEnum<Integer> {
    YES(0,"同意"),
    NO(1,"拒绝"),
    ;


    OpinionEnum(int value, String desc){
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


    private final Integer value;
    //    @JsonValue
    private final String desc;
}


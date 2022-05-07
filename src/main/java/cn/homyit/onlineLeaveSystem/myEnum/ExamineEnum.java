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
    FAILURE(-1,"拒绝"),
    SUCCESS(3,"同意")
    ;


    ExamineEnum(int value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public static ExamineEnum getEumByCode(int value){
        for(ExamineEnum enumItem: ExamineEnum.values()) {
            if(enumItem.getValue().intValue() ==value) {
                return enumItem;
            }
        }
        return null;
    }

//
//    public static void main(String[] args) {
//        System.out.println(ExamineEnum.getEumByCode(2));
//    }

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

package cn.homyit.onlineLeaveSystem.myEnum;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author 州牧
 * @description
 * @since 2022-05-04 11:07
 */
public enum ExamineEnum implements IEnum<Integer> {
    INSTRUCTOR(0,"辅导员审核中"),
    SECRETARY(1,"负责人审核中"),
    DEAN(2,"院长审核中"),



    WAIT_REPORT(3,"待销假"),
    APPLY_EXPIRED(10,"申请假条已过期"),
    REPORT_EXPIRED(11,"销假已过期"),
    PROCESSING(12,"处理中"),
    PROCESSED(14,"已销假"),
    FAILURE(-1,"拒绝")

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

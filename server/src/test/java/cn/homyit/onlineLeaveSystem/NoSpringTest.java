package cn.homyit.onlineLeaveSystem;

import cn.homyit.onlineLeaveSystem.myEnum.ExamineEnum;
import cn.homyit.onlineLeaveSystem.myEnum.LevelEnum;
import org.junit.jupiter.api.Test;

/**
 * @author 州牧
 * @description
 * @since 2022-05-05 11:34
 */

public class NoSpringTest {
    @Test
    void test06(){
        LevelEnum role = LevelEnum.DEAN;
        System.out.println(role.equals(LevelEnum.DEAN));
        System.out.println(role.equals(LevelEnum.LOOK));
    }

    @Test
    void test07(){
        System.out.println(LevelEnum.DEAN.getValue() > LevelEnum.INSTRUCTOR.getValue());
    }

    @Test
    void test08(){
        System.out.println("202026202014".substring(6, 12));
    }

    @Test
    void test09(){
//        System.out.println(ExamineEnum.valueOf(ExamineEnum.INSTRUCTOR.getValue()+1));

        System.out.println(ExamineEnum.getEumByCode(2));

    }

    @Test
    void test10(){
        System.out.println(ExamineEnum.getEumByCode(LevelEnum.INSTRUCTOR.getValue()-1));
    }
}


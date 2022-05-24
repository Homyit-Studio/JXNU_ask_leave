package cn.homyit.onlineLeaveSystem.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 通用错误枚举（不同类型的错误也可以拆成不同的Enum细分）
 *
 */
@Getter
public enum ExceptionCodeEnum {

    /*
    权限处理
    * */
    DELETED_NOTE(600,"假条已通过,无法删除"),
    ALREADY_AGREE(601,"您已经审核"),
    UPLOAD_ERROR(603,"上传失败"),
    NO_IMAGES(604,"此假条未上传图片"),
    NOT_IMAGE(605,"请上传图片"),
    NO_CLASS_ID(606,"请添加学生对应班级或年级"),
    TOKEN_EXPIRED(567,"登录过期，请重新登录"),
    FORBIDDEN(568,"权限不足"),

    /**
     * 通用结果
     */
    ERROR(-1, "网络错误"),
    SUCCESS(200, "成功"),

    /**
     * 用户登录
     */
    NEED_LOGIN(900, "用户未登录"),
    LOGIN_ERROR(901,"用户名或密码错误"),
//    PWD_ERROR(902,"账户已经锁定"),
    UPDATE_PWD_ERROR(903,"原密码输入错误"),


    NO_USER(800,"用户不存在"),
    ADD_USER(801,"角色增加错误，请您重试"),
    USER_EXIST(802,"用户已存在"),

    NO_CLASS(803,"您无管理班级"),

    /**
     * 参数校验
     */
    ERROR_PARAM(10000, "参数错误"),
    EMPTY_PARAM(10001, "参数为空"),
    ERROR_PARAM_LENGTH(10002, "参数长度错误");

    private final Integer code;
    private final String desc;

    ExceptionCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ExceptionCodeEnum> ENUM_CACHE = new HashMap<>();

    static {
        for (ExceptionCodeEnum exceptionCodeEnum : ExceptionCodeEnum.values()) {
            ENUM_CACHE.put(exceptionCodeEnum.code, exceptionCodeEnum);
        }
    }

    public static String getDesc(Integer code) {
        return Optional.ofNullable(ENUM_CACHE.get(code))
                .map(ExceptionCodeEnum::getDesc)
                .orElseThrow(() -> new IllegalArgumentException("invalid exception code!"));
    }

}
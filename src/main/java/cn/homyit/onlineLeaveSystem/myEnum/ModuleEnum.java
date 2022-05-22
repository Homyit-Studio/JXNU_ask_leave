package cn.homyit.onlineLeaveSystem.myEnum;

import lombok.Getter;

/**
 * 系统模块枚举类
 */
@Getter
public enum ModuleEnum {

	NOTE("假条"),

	IMAGE("图片"),

	EXCEL("excel"),

//	NOTE_PROCESSING("处理请假"),

	MANAGE_USER("管理人员");

	private final String moduleCode;

	ModuleEnum(String moduleCode) {
		this.moduleCode = moduleCode;
	}

}
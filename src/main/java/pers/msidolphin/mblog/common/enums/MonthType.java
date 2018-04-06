package pers.msidolphin.mblog.common.enums;

/**
 * 月份枚举
 * Created by msidolphin on 2018/4/6.
 */
public enum  MonthType {
	JANUARY(1, "一"),
	FEBRUARY(2, "二"),
	MARCH(3, "三"),
	APRIL(4, "四"),
	MAY(5,  "五"),
	JUNE(6, "六"),
	JULY(7, "七"),
	AUGUST(8, "八"),
	SEPTEMBER(9, "九"),
	OCTOBER(10, "十"),
	NOVEMBER(11, "十一"),
	DECEMBER(12, "十二");

	private String chinese;
	private Integer number;

	MonthType(Integer number, String chinese) {
		this.chinese = chinese;
		this.number = number;
	}

	public String getChinese() {
		return chinese;
	}

	public Integer getNumber() {
		return number;
	}

	public static String getChinese(Integer number){
		for(MonthType monthType : MonthType.values()) {
			if (monthType.getNumber() == number) return monthType.getChinese();
		}
		return null;
	}

}

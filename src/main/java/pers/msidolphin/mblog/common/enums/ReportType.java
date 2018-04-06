package pers.msidolphin.mblog.common.enums;

/**
 * Created by msidolphin on 2018/4/6.
 */
public enum ReportType {
	MONTH(0, "按月"),
	YEAR(1, "按年"),
	WEEK(2, "按周");

	private int    key;
	private String value;

	ReportType(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public ReportType setValue(String value) {
		this.value = value;
		return this;
	}

	public static ReportType get(int key) {
		for(ReportType type : ReportType.values()) {
			if(type.getKey() == key) {
				return type;
			}
		}
		return null;
	}

}

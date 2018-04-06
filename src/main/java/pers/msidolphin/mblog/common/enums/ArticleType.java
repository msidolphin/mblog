package pers.msidolphin.mblog.common.enums;

/**
 * 文章类型枚举
 * Created by msidolphin on 2018/3/30.
 */
public enum  ArticleType {
	ORIGINAL(0, "原创"),
	REPRINT(1, "转载"),
	TRANSLATE(2, "翻译");

	private int    key;
	private String value;

	ArticleType(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static ArticleType get(int key) {
		for(ArticleType type : ArticleType.values()) {
			if(type.getKey() == key) {
				return type;
			}
		}
		return null;
	}
}

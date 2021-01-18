package com.zhichan.openadsdk.holder;

/**
 * author : frankylee
 * date : 2021/1/18 10:09 AM
 * description : 广告类型
 * UNION 穿山甲
 * ADNET 广点通
 */
public enum AdType {
    UNION("union"),
    ADNET("adnet");

    private String typeName;

    AdType(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param typeName 类型名称
     */
    public static AdType fromTypeName(String typeName) {
        for (AdType type : AdType.values()) {
            if (type.getTypeName().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    public String getTypeName() {
        return this.typeName;
    }

}

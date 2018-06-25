package com.jbb.mgt.common.constant;

/**
 * @author jarome
 * @date 2018/4/29
 */
public enum AccountOpType {
    /**
     * 初审
     */
    INIT(1),
    /**
     *复审
     */
    FINAL(2),
    /**
     * 打款
     */
    LOAN(3);
    private Integer id;

    AccountOpType(Integer id) {
        this.id = id;
    }

    public int value() {
        return this.id;
    }
}

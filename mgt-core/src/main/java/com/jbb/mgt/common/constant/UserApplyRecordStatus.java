package com.jbb.mgt.common.constant;

/**
 * 用户申请记录表状态
 * @author jarome
 * @date 2018/4/30
 */
public enum UserApplyRecordStatus {
    /**
     * 无效的
     */
    INVALID(0),
    /**
     * 待初审
     */
    INIT_APPROVING(1),
    /**
     * 待复审
     */
    FINAL_APPROVEING(2),
    /**
     * 待放款
     */
    LOANING(3),
    /**
     * 完成
     */
    COMPLATE(4)
    ;

    private Integer id;

    UserApplyRecordStatus(Integer id) {
        this.id = id;
    }

    public Integer value(){
        return this.id;
    }
}

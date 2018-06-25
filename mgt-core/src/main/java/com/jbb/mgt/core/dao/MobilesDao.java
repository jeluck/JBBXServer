package com.jbb.mgt.core.dao;

import java.util.List;

import com.jbb.mgt.core.domain.Mobiles;

public interface MobilesDao {
    // 插入
    void insertMobiles(Mobiles mobiles);

    // 查询 按手机号和类型
    List<Mobiles> selectMobiles(String phoneNumber, String checkType);

}

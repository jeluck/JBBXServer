package com.jbb.mgt.core.dao.mapper;

import com.jbb.mgt.core.domain.UserContant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserContantMapper {

    void insertOrUpdateUserContant(@Param(value = "userContants")List<UserContant> userContants);

    List<UserContant> selectUserContantByUserId(@Param(value = "userId")Integer userId);

    void jbbToMgtInsertOrUpdateUserContant(@Param(value = "userContants")List<UserContant> userContants,@Param(value = "userId")Integer userId);
}

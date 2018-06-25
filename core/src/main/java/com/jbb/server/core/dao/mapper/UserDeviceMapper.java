package com.jbb.server.core.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.UserDevice;

public interface UserDeviceMapper {

    int saveUserDevice(UserDevice userDevice);

    List<UserDevice> selectUserDeviceListByUserId(@Param("userId") int userId);

    int updateUserDeviceStatus(@Param("userId") int userId , @Param("objectId") String objectId , @Param("status") boolean status);


}

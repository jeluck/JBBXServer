package com.jbb.server.core.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jbb.server.core.domain.Order;

public interface OrdersMapper {

    int insertUserProductCount(@Param("userId") int userId, @Param("productName") String productName,
        @Param("count") int count);

    Integer selectUserProductCountForUpdate(@Param("userId") int userId, @Param("productName") String productName);

    int increaseUserProductCount(@Param("userId") int userId, @Param("productName") String productName);

    int reduceUserProductCount(@Param("userId") int userId, @Param("productName") String productName);

    int insertOrder(Order order);

    int updateOrder(Order order);

    Order selectOrder(String tradeNo);

    Integer selectUserProductCount(@Param("userId") int userId, @Param("productName") String productName);

}

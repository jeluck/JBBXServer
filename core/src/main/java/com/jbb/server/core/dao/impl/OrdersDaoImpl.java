package com.jbb.server.core.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbb.server.core.dao.OrdersDao;
import com.jbb.server.core.dao.mapper.OrdersMapper;
import com.jbb.server.core.domain.Order;

@Repository("OrdersDao")
public class OrdersDaoImpl implements OrdersDao {

    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public boolean insertUserProductCount(int userId, String productName, int count) {
        return ordersMapper.insertUserProductCount(userId, productName, count) > 0;
    }

    @Override
    public int selectUserProductCountForUpdate(int userId, String productName) {
        Integer cnt = ordersMapper.selectUserProductCountForUpdate(userId, productName);
        return cnt == null ? 0 : cnt;
    }
    
    @Override
    public int selectUserProductCount(int userId, String productName) {
        Integer cnt = ordersMapper.selectUserProductCount(userId, productName);
        return cnt == null ? 0 : cnt;
    }

    @Override
    public boolean increaseUserProductCount(int userId, String productName) {
        return ordersMapper.increaseUserProductCount(userId, productName) > 0;
    }

    @Override
    public boolean reduceUserProductCount(int userId, String productName) {
        return ordersMapper.reduceUserProductCount(userId, productName) > 0;
    }

    @Override
    public boolean insertOrder(Order order) {
        return ordersMapper.insertOrder(order) > 0;
    }

    @Override
    public boolean upadateOrder(Order order) {
        return ordersMapper.updateOrder(order) > 0;
    }

    @Override
    public Order selectOrder(String tradeNo) {
        return ordersMapper.selectOrder(tradeNo);
    }

}

package com.jbb.server.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jbb.server.core.dao.OrdersDao;
import com.jbb.server.core.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private OrdersDao orderDao;

    @Override
    public int getProductCount(int userId, String productName) {
        return orderDao.selectUserProductCount(userId, productName);
    }

    
    @Override
    public int getProductCountForUpdate(int userId, String productName) {
        return orderDao.selectUserProductCountForUpdate(userId, productName);
    }
    
    
    @Override
    public void reduceProductCount(int userId, String productName) {
        int cnt = orderDao.selectUserProductCount(userId, productName);
        if(cnt==0){
            return;
        }
        orderDao.reduceUserProductCount(userId, productName);

    }

    @Override
    public void increaseUserProductCount(int userId, String productName) {
        orderDao.increaseUserProductCount(userId, productName);

    }

}

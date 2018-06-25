package com.jbb.server.core.service;

public interface ProductService {

    int getProductCount(int userId, String productName);

    void reduceProductCount(int userId, String productName);

    void increaseUserProductCount(int userId, String productName);

    int getProductCountForUpdate(int userId, String productName);

}

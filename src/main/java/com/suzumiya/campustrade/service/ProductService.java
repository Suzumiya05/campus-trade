package com.suzumiya.campustrade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.Product;

import java.util.List;

public interface ProductService {
    Page<Product> getByCategory(String category, int current, int size);
    Page<Product> getByPage(int current, int size);
    void addProduct(Product product, Long userId);
    void deleteById(Long id);
    Product getById(Long id);
    //登录用户查看“我的发布”
    Page<Product> getByUserId(Long userId, int current, int size);
}

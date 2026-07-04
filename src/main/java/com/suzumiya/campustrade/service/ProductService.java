package com.suzumiya.campustrade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.Product;


public interface ProductService {
    //分类+分页
    Page<Product> getByCategory(String category, int current, int size);

    //分页查全部
    Page<Product> getByPage(int current, int size);

    //添加商品
    void addProduct(Product product, Long userId);

    //按id删除
    void deleteById(Long id);

    //按id查商品
    Product getById(Long id);

    //登录用户查看“我的发布”
    Page<Product> getByUserId(Long userId, int current, int size);

    //关键词搜索商品
    Page<Product> searchProduct(String keyword, int current, int size);
}

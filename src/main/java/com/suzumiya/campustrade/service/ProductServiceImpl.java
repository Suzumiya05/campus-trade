package com.suzumiya.campustrade.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.Product;
import com.suzumiya.campustrade.entity.User;
import com.suzumiya.campustrade.mapper.ProductMapper;
import com.suzumiya.campustrade.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<Product> getByCategory(String category, int current, int size) {
        QueryWrapper<Product> qw = new QueryWrapper<>();
        Page<Product> page = new Page<>(current, size);
        qw.eq("category",category);
        productMapper.selectPage(page, qw);
        return page;
    }

    @Override
    public Page<Product> getByPage(int current, int size) {
        Page<Product> page = new Page<>(current,size);
        productMapper.selectPage(page, null);
        return page;
    }

    @Override
    public void addProduct(Product product, Long userId) {
        //取昵称(controller中获取userId传过来)
        User user = userMapper.selectById(userId);
        product.setSellerName(user.getNickname());
        productMapper.insert(product);
    }

    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public Page<Product> getByUserId(Long userId, int current, int size) {
        QueryWrapper<Product> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        Page<Product> page = new Page<>(current,size);
        productMapper.selectPage(page, qw);
        return page;
    }
}

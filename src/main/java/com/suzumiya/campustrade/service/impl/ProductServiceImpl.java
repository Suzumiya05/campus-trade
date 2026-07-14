package com.suzumiya.campustrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.AuditResult;
import com.suzumiya.campustrade.entity.Product;
import com.suzumiya.campustrade.entity.User;
import com.suzumiya.campustrade.exception.BusinessException;
import com.suzumiya.campustrade.mapper.ProductMapper;
import com.suzumiya.campustrade.mapper.UserMapper;
import com.suzumiya.campustrade.service.ContentAuditService;
import com.suzumiya.campustrade.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContentAuditService contentAuditService;

    //分类+分页
    @Override
    public Page<Product> getByCategory(String category, int current, int size) {
        QueryWrapper<Product> qw = new QueryWrapper<>();
        Page<Product> page = new Page<>(current, size);
        qw.eq("category",category);
        productMapper.selectPage(page, qw);
        return page;
    }

    //分页查全部
    @Override
    public Page<Product> getByPage(int current, int size) {
        Page<Product> page = new Page<>(current,size);
        productMapper.selectPage(page, null);
        return page;
    }

    //添加商品
    @Override
    public void addProduct(Product product, Long userId) {
        //取昵称(controller中获取userId传过来)
        User user = userMapper.selectById(userId);
        product.setSellerName(user.getNickname());

        // 先经过AI审核
        AuditResult result = contentAuditService.audit(product);
        if (!result.isPassed()) {
            throw new BusinessException(400, result.getReason());
        }
        productMapper.insert(product);
    }

    //按id删除
    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
    }

    //按id查询
    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    //分页查看用户发布商品
    @Override
    public Page<Product> getByUserId(Long userId, int current, int size) {
        QueryWrapper<Product> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);
        Page<Product> page = new Page<>(current,size);
        productMapper.selectPage(page, qw);
        return page;
    }

    //关键词搜索商品
    @Override
    public Page<Product> searchProduct(String keyword, int current, int size) {
        QueryWrapper<Product> qw = new QueryWrapper<>();
        if (keyword != null && !keyword.equals("")) {
            qw.like("title",keyword).or().like("description",keyword);
        }
        Page<Product> page = new Page<>(current, size);
        return productMapper.selectPage(page, qw);
    }
}

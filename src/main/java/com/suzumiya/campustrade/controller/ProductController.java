package com.suzumiya.campustrade.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.Product;
import com.suzumiya.campustrade.entity.R;
import com.suzumiya.campustrade.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@Validated 是 Spring 提供的，用于开启方法级别的校验
@Validated
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    //分类+分页查看
    @GetMapping(params = {"category", "current", "size"})
    public R<Page<Product>> getByCategory(
            @RequestParam String category,
            @RequestParam @Min(value = 1, message = "页码必须大于0") Integer current,
            @RequestParam @Min(value = 1, message = "每页大小必须大于0")
            @Max(value = 100, message = "每页大小不能超过100") Integer size
    ){
        return R.ok(productService.getByCategory(category, current, size));
    }

    //分页查全部
    @GetMapping("/page")
    public R<Page<Product>> getByPage(
            @RequestParam @Min(value = 1, message = "页码必须大于0") Integer current,
            @RequestParam @Min(value = 1, message = "每页大小必须大于0")
            @Max(value = 100, message = "每页大小不能超过100") Integer size
    ){
        return R.ok(productService.getByPage(current, size));
    }

    //按id查商品
    @GetMapping("/{id}")
    public R<Product> getById(@PathVariable Long id){
        Product product = productService.getById(id);
        if (product == null){
            return R.error("商品不存在");
        }
        return R.ok(product);
    }

    //删除商品
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id){
        productService.deleteById(id);
        return R.ok();
    }

    //发布商品
    @PostMapping("/release")
    public R<String> save(@Valid @RequestBody Product product, HttpServletRequest request){
        //controller内只需给商品设置userId，service里才设置昵称
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            return R.error("请先登录");
        }
        product.setUserId(userId);
        productService.addProduct(product, userId);
        return R.ok("商品发布成功");
    }

    //用户查询“我的发布”
    @GetMapping("/my_release")
    public R<Page<Product>> getMyRelease(
            HttpServletRequest request,
            @RequestParam @Min(value = 1, message = "页码必须大于0") Integer current,
            @RequestParam @Min(value = 1, message = "每页大小必须大于0")
            @Max(value = 100, message = "每页大小不能超过100") Integer size
    ){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            return R.error("请先登录");
        }
        return R.ok(productService.getByUserId(userId, current, size));
    }

    //搜索商品
    @GetMapping("/search")
    public R<Page<Product>> searchProduct(
            @RequestParam(required = false) String keyword,
            @RequestParam @Min(value = 1, message = "页码必须大于0") Integer current,
            @RequestParam @Min(value = 1, message = "每页大小必须大于0")
            @Max(value = 100, message = "每页大小不能超过100") Integer size
    ){
        return R.ok(productService.searchProduct(keyword, current, size));
    }
}

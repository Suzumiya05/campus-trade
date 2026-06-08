package com.suzumiya.campustrade.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.Product;
import com.suzumiya.campustrade.entity.R;
import com.suzumiya.campustrade.entity.User;
import com.suzumiya.campustrade.service.ProductService;
import com.suzumiya.campustrade.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(params = {"category", "current", "size"})
    public R<Page<Product>> getByCategory(@RequestParam String category,
                                          @RequestParam Integer current, @RequestParam Integer size){
        return R.ok(productService.getByCategory(category, current, size));
    }

    @GetMapping("/page")
    public R<Page<Product>> getByPage(@RequestParam Integer current, @RequestParam Integer size){
        return R.ok(productService.getByPage(current, size));
    }

    @GetMapping("/{id}")
    public R<Product> getById(@PathVariable Long id){
        Product product = productService.getById(id);
        if (product == null){
            return R.error("商品不存在");
        }
        return R.ok(product);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id){
        productService.deleteById(id);
        return R.ok();
    }

    @PostMapping("/release")
    public R<String> save(@RequestBody Product product, HttpServletRequest request){
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
    public R<Page<Product>> getMyRelease(HttpServletRequest request, @RequestParam Integer current,
                                         @RequestParam Integer size){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null){
            return R.error("请先登录");
        }
        return R.ok(productService.getByUserId(userId, current, size));
    }
}

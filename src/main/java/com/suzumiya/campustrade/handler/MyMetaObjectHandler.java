package com.suzumiya.campustrade.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // 1. 必须将处理器注册为Spring Bean
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 2. 重写插入时的填充逻辑
    @Override
    public void insertFill(MetaObject metaObject) {
        // 推荐使用 strictInsertFill 方法，更严格、更安全，会自动校验字段类型[9†L13-L14][12†L13-L14]
        // 参数：元对象, 字段名, 字段类型, 要填充的值[5†L11-L12]
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    // 3. 重写更新时的填充逻辑
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
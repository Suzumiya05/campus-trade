package com.suzumiya.campustrade.service;

import com.suzumiya.campustrade.entity.AuditResult;
import com.suzumiya.campustrade.entity.Product;

/**
 * 用于审核商品内容，接入真实 AI
 */
public interface ContentAuditService {

    //AI 审核服务
    AuditResult audit(Product product);
}

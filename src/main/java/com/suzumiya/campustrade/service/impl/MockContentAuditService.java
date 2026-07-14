package com.suzumiya.campustrade.service.impl;

import com.suzumiya.campustrade.entity.AuditResult;
import com.suzumiya.campustrade.entity.Product;
import com.suzumiya.campustrade.service.ContentAuditService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockContentAuditService implements ContentAuditService {
    // 模拟AI审核服务
    @Override
    public AuditResult audit(Product product) {
        return AuditResult.pass();
    }
}

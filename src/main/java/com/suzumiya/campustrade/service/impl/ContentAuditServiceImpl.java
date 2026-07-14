package com.suzumiya.campustrade.service.impl;

import com.suzumiya.campustrade.client.PythonAuditClient;
import com.suzumiya.campustrade.dto.AuditResponseDTO;
import com.suzumiya.campustrade.entity.AuditResult;
import com.suzumiya.campustrade.entity.Product;
import com.suzumiya.campustrade.service.ContentAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class ContentAuditServiceImpl implements ContentAuditService {
    private final PythonAuditClient pythonAuditClient;

    @Override
    public AuditResult audit(Product product) {
        // 调用python客户端
        AuditResponseDTO response = pythonAuditClient.auditProduct(
                product.getTitle(),
                product.getDescription() != null ? product.getDescription() : ""
        );
        // 转换为通用AuditResult
        if (response.isPassed()) {
            return AuditResult.pass();
        } else {
            return AuditResult.fail(response.getReason());
        }
    }
}

package com.suzumiya.campustrade.client;

import com.suzumiya.campustrade.dto.AuditRequestDTO;
import com.suzumiya.campustrade.dto.AuditResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PythonAuditClient {

    private final RestTemplate restTemplate;

    @Value("${python.audit.url}")
    private String auditUrl;

    /**
     * 调用 Python 审核服务，若失败则返回降级结果（直接放行）
     */
    public AuditResponseDTO auditProduct(String title, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            AuditRequestDTO request = new AuditRequestDTO();
            request.setTitle(title);
            request.setDescription(description);
            HttpEntity<AuditRequestDTO> entity = new HttpEntity<>(request, headers);

            AuditResponseDTO response = restTemplate.postForObject(auditUrl, entity, AuditResponseDTO.class);
            log.info("Python 审核结果：passed={}, reason={}", response.isPassed(), response.getReason());
            return response;
        } catch (ResourceAccessException e) {
            log.error("调用 Python 审核服务失败（连接超时或拒绝）：{}", e.getMessage());
            return fallbackResponse("审核服务暂不可用，自动放行");
        } catch (Exception e) {
            log.error("调用 Python 审核服务未知异常：{}", e.getMessage());
            return fallbackResponse("审核异常，自动放行");
        }
    }

    private AuditResponseDTO fallbackResponse(String reason) {
        AuditResponseDTO dto = new AuditResponseDTO();
        dto.setPassed(true);   // 降级策略：放行
        dto.setReason(reason);
        return dto;
    }
}
package com.suzumiya.campustrade.entity;

import lombok.Data;

@Data
public class AuditResult {
    boolean passed;//是否通过审核
    String reason;//审核不通过原因

    //快速生成通过
    public static AuditResult pass() {
        AuditResult r = new AuditResult();
        r.passed = true;
        return r;
    }

    //快速生成不通过
    public static AuditResult fail(String reason) {
        AuditResult r = new AuditResult();
        r.passed = false;
        r.reason = reason;
        return r;
    }
}

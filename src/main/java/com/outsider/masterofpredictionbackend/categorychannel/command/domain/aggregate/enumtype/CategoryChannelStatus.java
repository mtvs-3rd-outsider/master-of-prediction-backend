package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype;

/**
 * 카테고리 채널의 상태 enum
 * @see #APPLY
 * @see #APPROVED
 * @see #REJECTED
 * @see #ACTIVE
 * @see #INACTIVE
 */
public enum CategoryChannelStatus {
    /**
     * 카테고리 채널 등록 신청
     */
    APPLY,

    /**
     * 카테고리 채널 등록 승인
     */
    APPROVED,

    /**
     * 카테고리 채널 등록 거절
     */
    REJECTED,

    /**
     * 카테고리 채널 활성화
     */
    ACTIVE,

    /**
     * 카테고리 채널 비활성화
     */
    INACTIVE;
}

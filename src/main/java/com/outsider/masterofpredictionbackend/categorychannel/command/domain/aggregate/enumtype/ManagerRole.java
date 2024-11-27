package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype;


public enum ManagerRole {
    OWNER, // 채널 소유자, 최고 권한
    ASSISTANT_MANAGER, // 일반적인 부매니저
    CONTENT_MANAGER, // 게시글, 미디어, 콘텐츠 관련 관리
    MEMBER_MANAGER, // 멤버 초대, 강퇴 등 사용자 관리
    SETTINGS_MANAGER // 채널 설정 관리
}

package com.nhnacademy.miniDooray.common;

public enum ProjectStatus {
    ACTIVATED("활성"),
    DORMANT("휴면"),
    CLOSED("종료");

    private final String koreanValue;

    ProjectStatus(String koreanValue) {
        this.koreanValue = koreanValue;
    }

    public String getKoreanValue() {
        return koreanValue;
    }
}

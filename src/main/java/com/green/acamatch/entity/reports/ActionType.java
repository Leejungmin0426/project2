package com.green.acamatch.entity.reports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {
    no_action("활동유지", 0),
    one_week_ban("7일 정지", 7),
    two_week_ban("14일 정지", 14),
    one_month_ban("한달 정지",30),
    one_year_ban("일년 정지", 365),
    forever_ban("영구 정지", Integer.MAX_VALUE);

    private final String description;
    private final int durationDays;
}

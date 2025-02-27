package com.green.acamatch.acaClass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AcaClassDto {
    @Schema(title = "강좌 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long classId;
    @Schema(title = "학원 사진 여러장")
    private String acaPics;
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "강좌 이름", example = "초등 국어")
    private String className;
    @Schema(title = "수업 시작 날짜", example = "2025-01-16")
    private String startDate;
    @Schema(title = "수업 종료 날짜", example = "2025-01-30")
    private String endDate;
    @Schema(title = "검색어", example = "과학 창의 반")
    private String keyword;

    private String name;
}

package com.shiftm.shiftm.infra.holiday;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public record HolidayResponse(
        Response response
) {
    public record Response(
        Header header,
        Body body
    ) {}

    public record Header(
            String resultCode,  // 결과 코드 (예: "00")
            String resultMsg  // 결과 메시지 (예: "NORMAL SERVICE.")
    ) {}

    public record Body(
            Items items         // 공휴일 항목
    ) {}

    @JsonDeserialize(using = ItemsDeserializer.class)
    public record Items(
            List<Item> item     // 여러 개의 공휴일 항목
    ) {}

    public record Item(
            String locdate,     // 공휴일 날짜 (yyyyMMdd)
            int seq,            // 순번
            String dateKind,    // 공휴일 종류 (예: "01")
            String isHoliday,   // 공공기관 휴일 여부 (Y/N)
            String dateName     // 공휴일 명칭 (예: "삼일절")
    ) {}
}

package com.daesang.springbatch.dwrs.scemployee.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DwrsScEmployeeRestDto {
    private List<DwrsScEmployeeMappingDto> data;
    private String code;
}

package com.daesang.springbatch.dwrs.scemployee.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
public class DwrsScEmployeeReaderDto {
    private List<DwrsScEmployeeMappingDto> data;
}

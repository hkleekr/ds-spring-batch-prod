package com.daesang.springbatch.dwrs.scemployeemaster.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@Data
public class DwrsScEmployeeMasterReaderDto {
    private List<DwrsScEmployeeMasterMappingDto> data;
}

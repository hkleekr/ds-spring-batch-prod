package com.daesang.springbatch.dwrs.scemployeeaccount.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DwrsScEmployeeAccountRestDto {
    private List<DwrsScEmployeeAccountMappingDto> data;
   // private String code;
}

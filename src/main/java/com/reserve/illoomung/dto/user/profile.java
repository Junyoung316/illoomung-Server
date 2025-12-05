package com.reserve.illoomung.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class profile {
    private Long accountId;
    private String nickName;
}

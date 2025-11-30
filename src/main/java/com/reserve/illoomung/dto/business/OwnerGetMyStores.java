package com.reserve.illoomung.dto.business;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OwnerGetMyStores {
    private Long StoreId;
    private String StoreName;
    private String StoreAddr;
}

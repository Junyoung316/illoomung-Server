package com.reserve.illoomung.dto.reserve.owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerGetReserveInfo {
    private int ReserveCount;
    private List<OwnerGetReserveInfoResponse> Reserve;
}

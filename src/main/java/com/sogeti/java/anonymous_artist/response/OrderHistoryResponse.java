package com.sogeti.java.anonymous_artist.response;

import com.sogeti.java.anonymous_artist.dto.OrderItemDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderHistoryResponse(
        UUID orderID,
        LocalDateTime orderDate,
        BigDecimal totalOrderPrice,
        List<OrderItemDto> orderItemDtoList
) {
}
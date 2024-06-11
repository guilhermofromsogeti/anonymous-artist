package com.sogeti.java.anonymous_artist.controller;

import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.mapper.OrderMapper;
import com.sogeti.java.anonymous_artist.request.OrderRequest;
import com.sogeti.java.anonymous_artist.response.OrderHistoryResponse;
import com.sogeti.java.anonymous_artist.response.OrderResponse;
import com.sogeti.java.anonymous_artist.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/anonymous-artist/api/order/")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderResponse> createAnOrderFromCart(@RequestBody @Valid OrderRequest orderRequest) {
        OrderDto orderDto = OrderMapper.fromOrderRequestToDto(orderRequest);
        OrderDto savedOrderDto = orderService.createAnOrderForAUser(orderDto);
        OrderResponse orderResponse = OrderMapper.fromOrderDtoToOrderResponse(savedOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("history/")
    public ResponseEntity<?> getOrderHistory() {
        List<OrderHistoryResponse> responseList = orderService.getOrderHistory().stream().map(OrderMapper::fromOrderHistoryDtoToOrderHistoryResponse).toList();
        if (responseList.isEmpty()) {
            return ResponseEntity.ok().body("It seems that you don't have a purchase history.");
        }
        return ResponseEntity.ok().body(responseList);
    }
}

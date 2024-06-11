package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.dto.OrderHistoryDto;
import com.sogeti.java.anonymous_artist.mapper.CartMapper;
import com.sogeti.java.anonymous_artist.mapper.OrderMapper;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Order;
import com.sogeti.java.anonymous_artist.model.OrderItem;
import com.sogeti.java.anonymous_artist.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Transactional
    public OrderDto createAnOrderForAUser(OrderDto orderDto) {
        CartDto cartDto = getCartDetails();
        Cart cart = CartMapper.fromCartDtoToCart(cartDto);
        Order order = createOrderFromDtoAndCart(orderDto, cartDto);
        mapCartItemsToOrderItems(cart, order);
        order.setTotalPrice(cart.getTotalPrice());
        Order savedOrder = saveOrder(order);
        clearCartItemsInCart(cartDto);
        return OrderMapper.fromOrderToOrderDto(savedOrder);
    }

    private CartDto getCartDetails() {
        return cartService.getOverviewCurrentCart();
    }

    private Order createOrderFromDtoAndCart(OrderDto orderDto, CartDto cartDto) {
        return OrderMapper.fromOrderDtoAndCartDtoToOrder(orderDto, cartDto);
    }

    private void mapCartItemsToOrderItems(Cart cart, Order order) {
        for (CartItem cartItem : cart.getCartItemList()) {
            OrderItem orderItem = OrderMapper.fromCartItemToOrderItem(cartItem);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
    }

    private Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    private void clearCartItemsInCart(CartDto cartDto) {
        cartDto.cartItemList().clear();
    }

    public List<OrderHistoryDto> getOrderHistory() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Order> orderList = orderRepository.findByEmail(username);
        return orderList.stream().map(OrderMapper::fromOrderToOrderHistoryDto).toList();
    }
}

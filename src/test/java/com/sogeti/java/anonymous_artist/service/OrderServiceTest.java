package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.OrderDto;
import com.sogeti.java.anonymous_artist.dto.OrderHistoryDto;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.factory.OrderFactory;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Order;
import com.sogeti.java.anonymous_artist.model.OrderItem;
import com.sogeti.java.anonymous_artist.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    CustomUserDetailsService userDetailsService;

    @Mock
    CartService cartService;

    @InjectMocks
    OrderService orderService;


    @Test
    void givenValidCartAndOrderDto_whenCreatingAnOrder_thenANewOrderIsCreated() {
        // Given
        UUID cartId = UUID.randomUUID();
        CartDto cartDto = CartFactory.createMockCartDto();
        Order order = OrderFactory.anOrder().build();
        OrderDto orderDto = OrderFactory.fromOrderToOrderDto(order);

        when(cartService.getOverviewCurrentCart()).thenReturn(cartDto);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        OrderDto result = orderService.createAnOrderForAUser(orderDto);

        // Then
        assertNotNull(result);
        assertNotNull(result.fullName());

        verify(cartService).getOverviewCurrentCart();
        verify(orderRepository).save(any(Order.class));
    }


    @Test
    void givenValidOrderDtoAndValidCartDto_whenMappingToOrder_ThenOrderMapperMapsOrderDtoAndCardDtoToOrder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        OrderDto orderDto = OrderFactory.createMockOrderDto();
        CartDto cartDto = CartFactory.createMockCartDto();

        // When (invoke private method using reflection)
        Method method = OrderService.class.getDeclaredMethod("createOrderFromDtoAndCart", OrderDto.class, CartDto.class);
        method.setAccessible(true);
        Order result = (Order) method.invoke(orderService, orderDto, cartDto);

        // Then
        assertNotNull(result);
    }


    @Test
    void givenCartItems_whenMappingToOrderItems_thenAllFieldsAreMappedCorrectly() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        CartItem cartItem1 = CartFactory.aCartItem().build();
        CartItem cartItem2 = CartFactory.aCartItem().build();
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

        Cart cart = new Cart();
        cart.setCartItemList(cartItems);

        Order order = new Order();

        // When (invoke private method using reflection)
        Method method = OrderService.class.getDeclaredMethod("mapCartItemsToOrderItems", Cart.class, Order.class);
        method.setAccessible(true);
        method.invoke(orderService, cart, order);

        // Then
        List<OrderItem> orderItems = order.getOrderItems();
        assertNotNull(orderItems);
        assertEquals(cartItems.size(), orderItems.size());

        assertEquals(cartItem1.getProductTitle(), orderItems.getFirst().getProductTitle());
        assertEquals(cartItem1.getQuantity(), orderItems.get(0).getQuantity());
        assertEquals(cartItem1.getSubTotalPrice(), orderItems.get(0).getSubTotalPrice());
        assertEquals(cartItem2.getProductTitle(), orderItems.get(1).getProductTitle());
        assertEquals(cartItem2.getQuantity(), orderItems.get(1).getQuantity());
        assertEquals(cartItem2.getSubTotalPrice(), orderItems.get(1).getSubTotalPrice());
    }

    @Test
    void givenValidOrder_whenSavingOrderInRepository_thenOrderIsSavedInRepository() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // Given
        Order order = OrderFactory.anOrder().build();

        // When
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Method method = OrderService.class.getDeclaredMethod("saveOrder", Order.class);
        method.setAccessible(true);
        Order result = (Order) method.invoke(orderService, order);

        // Then
        assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void givenValidUserName_andListWithOrders_thenListOrderHistoryDtoIsShown() {
        // Given
        String userName = "test@user.com";
        UserDetails userDetails = mock(UserDetails.class);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        OrderItem orderItem1 = OrderFactory.anOrderItem();
        OrderItem orderItem2 = OrderFactory.anOrderItem();
        List<OrderItem> orderItemList = List.of(orderItem1, orderItem2);
        Order order1 = OrderFactory.anOrder().email(userName).orderItems(orderItemList).build();
        Order order2 = OrderFactory.anOrder().email(userName).orderItems(orderItemList).build();
        List<Order> orderList = List.of(order1, order2);

        // When
        when(userDetails.getUsername()).thenReturn(userName);
//        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(orderRepository.findByEmail(userName)).thenReturn(orderList);

        // Then
        List<OrderHistoryDto> result = orderService.getOrderHistory();
        verify(orderRepository).findByEmail(userName);
        assertEquals(2, result.size());
    }

    @Test
    void ivenValidUserName_andListWithOrders_thenEmptyListIsReturned() {
        // Given
        String userName = "test@user.com";
        UserDetails userDetails = mock(UserDetails.class);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        when(userDetails.getUsername()).thenReturn(userName);
        when(orderRepository.findByEmail(userName)).thenReturn(List.of());

        // Execute
        List<OrderHistoryDto> result = orderService.getOrderHistory();

        // Verify
        verify(orderRepository).findByEmail(userName);
        assertTrue(result.isEmpty());
    }
}
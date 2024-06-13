package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.factory.AccountFactory;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.factory.ProductFactory;
import com.sogeti.java.anonymous_artist.factory.UserDetailsFactory;
import com.sogeti.java.anonymous_artist.factory.UserFactory;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.model.User;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import com.sogeti.java.anonymous_artist.repository.CartItemRepository;
import com.sogeti.java.anonymous_artist.repository.CartRepository;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    UUID membershipId;
    UUID id;
    String userName;
    UserDetails userDetails;
    CartItem cartItem;
    Cart cart;
    Account account;
    User user;
    String password;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setup() {
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        userName = "test@user.com";
        password = "Dit!sE3nW8wrd";
        membershipId = UUID.randomUUID();

        userDetails = UserDetailsFactory.userDetails(userName, password);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        account = AccountFactory.anAccount().email(userName).build();
        user = UserFactory.aUser().account(account).build();
        account.setUser(user);
        cart = CartFactory.aCart().user(user).build();
        user.setAccount(account);
        user.setCart(cart);
        cartItem = CartFactory.aCartItem().build();
    }

    @Test
    void givenANewCartToBeSaved_whenSavingACart_thenShouldReturnSavedCart() {
        // Given
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        Cart savedCart = cartService.saveCart(cart);

        // Then
        assertNotNull(cart);
        verify(cartRepository).save(savedCart);
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    void givenValidUserName_whenGettingCurrentCart_thenReturnCurrentUserCart() {
        // Given
        userName = "test@user.com";

        // When
        when(accountRepository.findById(userName)).thenReturn(Optional.of(account));

        when(cartRepository.findCartByUserMembershipId(account.getUser().getMembershipId())).thenReturn(cart);

        // Then
        Cart result = cartService.getCurrentUserCart();

        verify(accountRepository).findById(userName);
        assertEquals(cart, result);
    }

    @Test
    void givenCartAndId_whenLookingForOptionalCartItemFromCartById_thenFoundOptionalIsReturned() {
        // Given
        CartItem item1 = CartFactory.aCartItem().id(id).build();
        CartItem item2 = CartFactory.aCartItem().build();
        List<CartItem> cartItemList = List.of(item1, item2);
        cart = CartFactory.aCart().cartItemList(cartItemList).build();

        // When
        Optional<CartItem> result = cartService.returnOptionalCartItemFromCartById(cart, id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(item1, result.get());
    }

    @Test
    void givenCartAndId_whenLookingForOptionalCartItemFromCartById_thenOptionIsEmpty() {
        // Given
        CartItem item1 = CartFactory.aCartItem().id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).build();
        CartItem item2 = CartFactory.aCartItem().id(UUID.fromString("123e4567-e89b-12d3-a456-426614174023")).build();
        List<CartItem> cartItemList = List.of(item1, item2);
        cart = CartFactory.aCart().cartItemList(cartItemList).build();

        UUID nonExistingId = UUID.fromString("123e4567-e89b-12d3-a456-426614174024");

        // When
        Optional<CartItem> result = cartService.returnOptionalCartItemFromCartById(cart, nonExistingId);


        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void givenCartAndId_whenLookingForCartItemFromCartById_thenCartItemIsReturned() {
        // Given
        CartItem item1 = CartFactory.aCartItem().id(id).build();
        CartItem item2 = CartFactory.aCartItem().id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).build();
        List<CartItem> cartItemList = List.of(item1, item2);
        cart = CartFactory.aCart().cartItemList(cartItemList).build();

        // When
        CartItem result = cartService.returnCartItemFromCartById(cart, id);

        // Then
        assertEquals(item1, result);
    }

    @Test
    void givenCartAndId_whenLookingForCartItemFromCartById_thenNoDataFoundExceptionIsThrown() {
        // Given
        CartItem item1 = CartFactory.aCartItem().id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).build();
        CartItem item2 = CartFactory.aCartItem().id(UUID.fromString("123e4567-e89b-12d3-a456-426614174023")).build();
        List<CartItem> cartItemList = List.of(item1, item2);
        cart = CartFactory.aCart().cartItemList(cartItemList).build();

        UUID nonExistentId = UUID.fromString("123e4567-e89b-12d3-a456-426614174099");

        // When - Then
        assertThrows(NoDataFoundException.class, () -> {
            cartService.returnCartItemFromCartById(cart, nonExistentId);
        });
    }

    @Test
    void givenValidCartItemDto_whenAddingExistingCartItem_thenIncreaseQuantity() {
        // Given
        Product product = ProductFactory.aProduct().id(id).title("Test product").build();
        CartItemDto cartItemDto = CartFactory.aCartItemDto().id(id).quantity(1).build();

        // When
        when(accountRepository.findById(userName)).thenReturn(Optional.of(account));
        when(cartService.getCurrentUserCart()).thenReturn(cart);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        cartItemService.increaseQuantityExistingCartItem(cartItem, cartItemDto.quantity());
        cartService.addCartItemToCart(cartItemDto);

        // Then
        assertEquals(cartItem.getQuantity(), cartItemDto.quantity());
    }

    @Test
    void givenValidCartItemDto_whenCartItemDoesNotExist_thenCreateAndAddToCart() {
        // Given
        Product product = ProductFactory.aProduct().id(id).title("Test product").build();
        CartItemDto cartItemDto = CartFactory.aCartItemDto().id(id).quantity(1).build();

        // When
        when(accountRepository.findById(userName)).thenReturn(Optional.of(account));
        when(cartService.getCurrentUserCart()).thenReturn(cart);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        cartService.addCartItemToCart(cartItemDto);

        // Then
        verify(cartItemService).createCartItem(product, cartItemDto.quantity());
    }

    @Test
    void givenValidRequest_andQuantityZero_whenUpdateQuantityCartItem_thenRemoveCartItemFromCart() {
        // Given
        int quantityZero = 0;
        cartItem.setId(id);
        CartItemDto cartItemDto = CartFactory.aCartItemDto()
                .id(id)
                .quantity(quantityZero).build();
        cart.addCartItemToCartItemList(cartItem);

        // When
        when(accountRepository.findById(userName)).thenReturn(Optional.of(account));
        when(cartService.getCurrentUserCart()).thenReturn(cart);

        String result = cartService.updateQuantityCartItem(cartItemDto);

        // Then
        assertEquals("The cart item is removed.", result);
    }


    @Test
    void givenValidRequest_andNewQuantity_andNoCartItemFound_whenUpdateQuantityCartItem_thenReturnNoDataFoundException() {
        // Given
        int newQuantity = 5;
        CartItemDto cartItemDto = CartFactory.aCartItemDto()
                .id(id)
                .quantity(newQuantity).build();

        // When - Then
        assertThrows(NoDataFoundException.class, () -> {
            cartService.returnCartItemFromCartById(cart, id);
        });
    }

    @Test
    void givenNonExistingId_whenFindProductById_thenReturnFoundProduct() {
        // Given
        Product expectedProduct = ProductFactory.aProduct().build();

        // When
        when(productRepository.findById(id)).thenReturn(Optional.of(expectedProduct));
        Product result = cartService.findProductById(id);

        // Then
        assertEquals(expectedProduct, result);
    }

    @Test
    void givenNonExistingId_whenFindProductById_thenReturnNoDataFoundException() {
        // Given - When
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        Exception exception = assertThrows(NoDataFoundException.class, () -> {
            cartService.findProductById(id);
        });

        assertEquals("Cannot find data: Product with ID " + id + " not found", exception.getMessage());
    }

    @Test
    void givenCart_whenGetOverviewCurrentCart_thenUpdateCartAndReturnCartDto() {
        // Given
        CartDto expectedCartDto = CartFactory.createMockCartDto();

        // When
        when(cartRepository.findCartByUserMembershipId(account.getUser().getMembershipId())).thenReturn(cart);
        when(accountRepository.findById(userName)).thenReturn(Optional.of(account));

        CartDto result = cartService.getOverviewCurrentCart();

        // Then
        verify(cartItemService).updateCartItemPricesAndSubtotals();
        assertEquals(expectedCartDto, result);
    }

    @Test
    void givenCart_whenUpdateCartTotalPrice_thenCartTotalPriceIsUpdated() {
        // Given
        CartItem item1 = CartFactory.aCartItem().id(UUID.fromString("dc6d1a62-2db3-4f7d-97b0-80b4af812ea6")).subTotalPrice(BigDecimal.valueOf(100.00)).build();
        CartItem item2 = CartFactory.aCartItem().id(UUID.fromString("dc6d1a62-2db3-4f7d-97b0-80b4af812ea7")).subTotalPrice(BigDecimal.valueOf(200.00)).build();
        List<CartItem> cartItemList = List.of(item1, item2);
        BigDecimal newTotalPrice = item1.getSubTotalPrice().add(item2.getSubTotalPrice());

        // When
        when(cartItemRepository.findByCartCartId(cart.getCartId())).thenReturn(cartItemList);
        cartService.updateCartTotalPrice(cart);

        // Then
        verify(cartItemRepository).findByCartCartId(cart.getCartId());
        assertEquals(newTotalPrice, cart.getTotalPrice());
        verify(cartRepository).save(cart);
    }
}

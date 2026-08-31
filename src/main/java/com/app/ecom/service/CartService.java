package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final  CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public boolean  addToCart(String userId, CartItemRequest request) {
        //look for product
        Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
        if (optionalProduct.isEmpty())
            return false;
        Product product = optionalProduct.get();
        if (product.getStockQuantity() < request.getQuantity())
            return false;
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isEmpty())
            return false;
        User user = userOptional.get();

        CartItem existingCarItem = cartItemRepository.findByUserAndProduct(user, product);
        if (existingCarItem != null) {
            //Update the quantity
            existingCarItem.setQuantity(existingCarItem.getQuantity() + request.getQuantity());
            existingCarItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCarItem.getQuantity())));
            cartItemRepository.save(existingCarItem);
        } else{
                //create new cart item
                CartItem cartItem = new CartItem();
                cartItem.setUser(user);
                cartItem.setProduct(product);
                cartItem.setQuantity(request.getQuantity());
                cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
                cartItemRepository.save(cartItem);
            }
        return true;
    }
    @Transactional
    public boolean deleteItemFromCart(String userId, long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            return false;

        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isEmpty())
            return false;

        Product product = optionalProduct.get();
        User user = userOptional.get();

        cartItemRepository.deleteByUserAndProduct(user, product);

        return true;

    }


    public List<CartItem> getCart(String userId) {
        return  userRepository.findById(Long.valueOf(userId))
                        .map(cartItemRepository ::findByUser)
                .orElseGet(List::of);
            }
@Transactional
    public void clearCart(String userId) {
        userRepository.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser
        );
    }
}


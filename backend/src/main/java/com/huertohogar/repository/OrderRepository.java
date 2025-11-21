package com.huertohogar.repository;

import com.huertohogar.model.entity.Order;
import com.huertohogar.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    @Query("SELECT o FROM Order o WHERE o.user.email = :email ORDER BY o.orderDate DESC")
    List<Order> findByUserEmailOrderByOrderDateDesc(@Param("email") String email);
    
    Optional<Order> findByOrderIdAndUser(String orderId, User user);
    
    List<Order> findByStatus(Order.OrderStatus status);
}

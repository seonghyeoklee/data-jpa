package com.ddd.order.application;

import com.ddd.order.NoOrderException;
import com.ddd.order.domain.Order;
import com.ddd.order.domain.OrderNo;
import com.ddd.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderService {
    private final OrderRepository orderRepository;

    public CancelOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void cancel(OrderNo orderNo) {
        Order order = orderRepository.findById(orderNo)
                .orElseThrow(NoOrderException::new);

        order.cancel();
    }
}

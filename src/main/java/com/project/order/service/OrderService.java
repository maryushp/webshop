package com.project.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.Item;
import com.project.item.repository.ItemRepository;
import com.project.order.model.Order;
import com.project.order.model.OrderDTO;
import com.project.order.model.OrderRequest;
import com.project.order.repository.OrderRepository;
import static com.project.utils.exceptionhandler.ExceptionMessages.*;

import com.project.orderitem.model.OrderItem;
import com.project.orderitem.model.OrderItemDTO;
import com.project.orderitem.model.OrderItemPK;
import com.project.orderitem.repository.OrderItemRepository;
import com.project.user.model.Role;
import com.project.user.model.User;
import com.project.user.repository.UserRepository;
import com.project.utils.exceptionhandler.exceptions.NoSuchElemException;
import com.project.utils.mappers.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements CrudOrderService {
    private final OrderRepository orderRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderRequest orderRequest) {
        Set<OrderItemDTO> orderItems = orderRequest.getOrderItems();

        Set<OrderItem> orderItemSet = orderItems.stream().map(entityDtoMapper::toOrderItem).collect(Collectors.toSet());

        Map<Item, Integer> orderMap = makeOrderMap(orderItemSet);
        LocalDateTime dateNow = LocalDateTime.now();

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format(USER_NOT_FOUND, currentUser.getId())));

        Order savedOrder = orderRepository.save(
                Order.builder()
                        .user(dbUser)
                        .cost(countOrderCost(orderMap))
                        .creationDate(dateNow)
                        .build());
        Set<OrderItem> savedOrderItems = createOrderItemSet(orderMap, savedOrder);
        savedOrder.setOrderItems(savedOrderItems);
        return entityDtoMapper.toOrderDTO(savedOrder);
    }

    @Override
    public Page<OrderDTO> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(entityDtoMapper::toOrderDTO);
    }

    @Override
    public OrderDTO get(Long id) {
        return entityDtoMapper.toOrderDTO(orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format(ORDER_NOT_FOUND, id))
                ));
    }

    public Page<OrderDTO> getByUser(Long id, Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole().equals(Role.ADMIN) || id.equals(currentUser.getId())) {
            return orderRepository.findByUserId(id, pageable).map(entityDtoMapper::toOrderDTO);
        }

        throw new AccessDeniedException(FORBIDDEN);
    }

    @Override
    @Transactional
    public OrderDTO update(Long id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchElemException(MessageFormat.format(ORDER_NOT_FOUND, id));
        }
        orderRepository.deleteById(id);
    }

    private Map<Item, Integer> makeOrderMap(Set<OrderItem> orderItems) {
        Map<Item, Integer> orderMap = new HashMap<>();

        for (OrderItem orderItem: orderItems) {
            Item item =
                    itemRepository.findById(orderItem.getItem().getId())
                            .orElseThrow(() ->
                                    new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, orderItem.getItem().getId())));
            orderMap.put(item, orderItem.getAmount());
        }
        return orderMap;
    }

    private BigDecimal countOrderCost(Map<Item, Integer> orderMap) {
        BigDecimal cost = BigDecimal.ZERO;
        for (Map.Entry<Item, Integer> entry: orderMap.entrySet()) {
            Item item = entry.getKey();
            Integer amount = entry.getValue();
            BigDecimal entryCost = item.getPrice().multiply(BigDecimal.valueOf(amount));
            cost = cost.add(entryCost);
        }
        return cost;
    }

    private Set<OrderItem> createOrderItemSet(Map<Item, Integer> orderMap, Order dbOrder) {
        return orderMap.entrySet().stream().map(entry -> {
            Item item = entry.getKey();
            Integer amount = entry.getValue();
            OrderItemPK orderItemPK = new OrderItemPK(dbOrder.getId(), item.getId());
            return orderItemRepository.save(
                    new OrderItem(orderItemPK, dbOrder, item, amount));
        }).collect(Collectors.toSet());
    }
}
package com.project.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.Item;
import com.project.item.service.ItemService;
import com.project.order.model.Order;
import com.project.order.model.OrderDTO;
import com.project.order.repository.OrderRepository;
import static com.project.utils.exceptionhandler.ExceptionMessages.*;

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
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements CrudOrderService {
    private final OrderRepository orderRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final ItemService itemService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDto) {
        Order order = entityDtoMapper.toOrder(orderDto);

        List<Item> actualItems = order.getItems();
        order.setItems(itemService.getExistedItems(actualItems));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format(USER_NOT_FOUND, currentUser.getId())));
        order.setUser(dbUser);

        order.setCreationDate(LocalDateTime.now());

        BigDecimal cost = order.getItems().stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setCost(cost);

        return entityDtoMapper.toOrderDTO(orderRepository.save(order));
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
}
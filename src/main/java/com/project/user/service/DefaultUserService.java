package com.project.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.order.model.OrderDTO;
import com.project.order.service.OrderService;
import com.project.user.model.Role;
import com.project.user.model.User;
import com.project.user.model.UserDTO;
import com.project.user.repository.UserRepository;
import com.project.utils.exceptionhandler.exceptions.InvalidUpdateRequest;
import com.project.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.project.utils.mappers.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final OrderService orderService;

    @Override
    public Page<UserDTO> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(entityDtoMapper::toUserDTO);
    }

    @Override
    public UserDTO get(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.getRole().equals(Role.ADMIN) || id.equals(currentUser.getId())) {
            return entityDtoMapper.toUserDTO(userRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(MessageFormat.format(USER_NOT_FOUND_ID, id))));
        }

        throw new AccessDeniedException(FORBIDDEN);
    }

    @Override
    public Page<OrderDTO> getOrders(Long id, Pageable pageable) {
        return orderService.getByUser(id, pageable);
    }

    @Override
    public UserDTO update(Long id, JsonMergePatch patch) {
        User attemptingUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!id.equals(attemptingUser.getId())) {
            throw new AccessDeniedException(FORBIDDEN);
        }

        User dbUser = userRepository
                .findById(id)
                .orElseThrow(() -> new ElementNotFoundException(MessageFormat.format(USER_NOT_FOUND_ID, id)));

        UserDTO updatedUser = getUpdatedUser(patch, dbUser);
        updateFields(dbUser, updatedUser);

        return entityDtoMapper.toUserDTO(userRepository.save(dbUser));
    }

    private void updateFields(User dbUser, UserDTO updatedUser) {
        dbUser.setName(updatedUser.getName());
        dbUser.setSurname(updatedUser.getSurname());
    }

    private UserDTO getUpdatedUser(JsonMergePatch patch, User actualUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO updatedUser;
        try {
            JsonNode updatedJson = patch.apply(objectMapper.convertValue(actualUser, JsonNode.class));
            updatedUser = objectMapper.treeToValue(updatedJson, UserDTO.class);
        } catch (JsonProcessingException | JsonPatchException e) {
            throw new InvalidUpdateRequest(INVALID_USER_UPDATE);
        }
        return updatedUser;
    }
}
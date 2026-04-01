package com.neo.stayhub.service;

import com.neo.stayhub.domain.User;
import com.neo.stayhub.events.BeforeDeleteUser;
import com.neo.stayhub.model.UserDTO;
import com.neo.stayhub.repos.UserRepository;
import com.neo.stayhub.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public UserService(final UserRepository userRepository,
            final ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteUser(id));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setPasswordHash(user.getPasswordHash());
        userDTO.setEnabled(user.getEnabled());
        userDTO.setAccountLocked(user.getAccountLocked());
        userDTO.setEmailVerified(user.getEmailVerified());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setVersion(user.getVersion());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPasswordHash(userDTO.getPasswordHash());
        user.setEnabled(userDTO.getEnabled());
        user.setAccountLocked(userDTO.getAccountLocked());
        user.setEmailVerified(userDTO.getEmailVerified());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        user.setVersion(userDTO.getVersion());
        return user;
    }

}

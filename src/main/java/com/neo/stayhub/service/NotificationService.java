package com.neo.stayhub.service;

import com.neo.stayhub.domain.Notification;
import com.neo.stayhub.domain.User;
import com.neo.stayhub.events.BeforeDeleteUser;
import com.neo.stayhub.model.NotificationDTO;
import com.neo.stayhub.repos.NotificationRepository;
import com.neo.stayhub.repos.UserRepository;
import com.neo.stayhub.util.NotFoundException;
import com.neo.stayhub.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(final NotificationRepository notificationRepository,
            final UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationDTO> findAll() {
        final List<Notification> notifications = notificationRepository.findAll(Sort.by("id"));
        return notifications.stream()
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .toList();
    }

    public NotificationDTO get(final Long id) {
        return notificationRepository.findById(id)
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getId();
    }

    public void update(final Long id, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(final Long id) {
        final Notification notification = notificationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        notificationRepository.delete(notification);
    }

    private NotificationDTO mapToDTO(final Notification notification,
            final NotificationDTO notificationDTO) {
        notificationDTO.setId(notification.getId());
        notificationDTO.setType(notification.getType());
        notificationDTO.setSubject(notification.getSubject());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setDeliveryChannel(notification.getDeliveryChannel());
        notificationDTO.setStatus(notification.getStatus());
        notificationDTO.setSentAt(notification.getSentAt());
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        notificationDTO.setUser(notification.getUser() == null ? null : notification.getUser().getId());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
            final Notification notification) {
        notification.setType(notificationDTO.getType());
        notification.setSubject(notificationDTO.getSubject());
        notification.setMessage(notificationDTO.getMessage());
        notification.setDeliveryChannel(notificationDTO.getDeliveryChannel());
        notification.setStatus(notificationDTO.getStatus());
        notification.setSentAt(notificationDTO.getSentAt());
        notification.setCreatedAt(notificationDTO.getCreatedAt());
        final User user = notificationDTO.getUser() == null ? null : userRepository.findById(notificationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        notification.setUser(user);
        return notification;
    }

    @EventListener(BeforeDeleteUser.class)
    public void on(final BeforeDeleteUser event) {
        final ReferencedException referencedException = new ReferencedException();
        final Notification userNotification = notificationRepository.findFirstByUserId(event.getId());
        if (userNotification != null) {
            referencedException.setKey("user.notification.user.referenced");
            referencedException.addParam(userNotification.getId());
            throw referencedException;
        }
    }

}

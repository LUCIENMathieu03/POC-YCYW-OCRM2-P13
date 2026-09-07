package com.poc_ycyw.ycyw.services;

import com.poc_ycyw.ycyw.dto.ClientDTO;
import com.poc_ycyw.ycyw.dto.MessageDTO;
import com.poc_ycyw.ycyw.dto.SendMessageRequestDTO;
import com.poc_ycyw.ycyw.models.Message;
import com.poc_ycyw.ycyw.models.Utilisateur;
import com.poc_ycyw.ycyw.repository.MessageRepository;
import com.poc_ycyw.ycyw.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<MessageDTO> getConversationForCurrentClient() {
        Utilisateur currentUser = authService.getCurrentUser();
        return toDTOs(messageRepository.findConversationWithClient(currentUser.getId()));
    }

    public List<MessageDTO> getConversationWithClient(Integer clientId) {
        requireAdmin();
        return toDTOs(messageRepository.findConversationWithClient(clientId));
    }

    public List<ClientDTO> getAdminConversations() {
        requireAdmin();
        List<Integer> clientIds = messageRepository.findDistinctClientIds();
        return utilisateurRepository.findAllById(clientIds).stream()
                .map(client -> new ClientDTO(client.getId(), client.getEmail(), client.getName()))
                .toList();
    }

    public MessageDTO sendMessage(SendMessageRequestDTO dto) {
        Utilisateur currentUser = authService.getCurrentUser();

        Message message = new Message();
        message.setContent(dto.getContent());
        message.setUtilisateurId(currentUser.getId());

        if ("admin".equals(currentUser.getRole())) {
            if (dto.getDestinataire() == null) {
                throw new IllegalArgumentException("Le destinataire est requis pour un admin");
            }
            message.setDestinataire(dto.getDestinataire());
        } else {
            message.setDestinataire(null);
        }

        Message saved = messageRepository.save(message);
        MessageDTO messageDTO = toDTO(saved, currentUser);

        Integer clientId = "admin".equals(currentUser.getRole()) ? dto.getDestinataire() : currentUser.getId();
        messagingTemplate.convertAndSend("/topic/conversation/" + clientId, messageDTO);

        return messageDTO;
    }

    private void requireAdmin() {
        Utilisateur currentUser = authService.getCurrentUser();
        if (!"admin".equals(currentUser.getRole())) {
            throw new AccessDeniedException("Réservé aux admins");
        }
    }

    // Récupère en une seule requête tous les expéditeurs distincts de la conversation,
    // pour éviter de faire un appel à la base par message (N+1).
    private List<MessageDTO> toDTOs(List<Message> messages) {
        List<Integer> senderIds = messages.stream().map(Message::getUtilisateurId).distinct().toList();
        Map<Integer, Utilisateur> sendersById = utilisateurRepository.findAllById(senderIds).stream()
                .collect(Collectors.toMap(Utilisateur::getId, Function.identity()));

        return messages.stream()
                .map(message -> toDTO(message, sendersById.get(message.getUtilisateurId())))
                .toList();
    }

    private MessageDTO toDTO(Message message, Utilisateur sender) {
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getDateEnvoi(),
                sender.getId(),
                sender.getName(),
                sender.getEmail(),
                message.getDestinataire()
        );
    }
}

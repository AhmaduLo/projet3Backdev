package com.example.projetbackend.service;

import com.example.projetbackend.model.Message;
import com.example.projetbackend.model.Rental;
import com.example.projetbackend.model.User;
import com.example.projetbackend.repository.MessageRepository;
import com.example.projetbackend.repository.RentalRepository;
import com.example.projetbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    // ------Créer un message--------
    @Transactional
    public Message createMessage(Integer userId, Integer rentalId, String messageText) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Message message = new Message(rental, user, messageText);
        return messageRepository.save(message);
    }

    // ------recupere les messages par RentalID--------
    public List<Message> getMessagesByRental(Integer rentalId) {
        return messageRepository.findByRentalId(rentalId);
    }

    // ------recupere message par son user--------
    public List<Message> getMessagesByUser(Integer userId) {
        return messageRepository.findByUserId(userId);
    }

    // ------supprimer un message--------
    public void deleteMessage(Integer messageId) {
        messageRepository.deleteById(messageId);
    }
}

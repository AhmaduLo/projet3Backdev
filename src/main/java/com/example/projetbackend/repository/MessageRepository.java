package com.example.projetbackend.repository;

import com.example.projetbackend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByRentalId(Integer rentalId);
    List<Message> findByUserId(Integer userId);
}

package com.example.projetbackend.repository;

import com.example.projetbackend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByRentalId(Integer rentalId);
    List<Message> findByUserId(Integer userId);
}

package org.example.server.repositories;


import org.example.server.models.EntryDate;
import org.example.server.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryDateRepository extends JpaRepository<EntryDate, Long> {
    Optional<EntryDate> findById (Long id);
    List<EntryDate> findByUser(User user);

}
package org.example.server.repositories;


import org.example.server.models.UserLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {
    Optional<UserLibrary> findById(Long id);
    Optional<UserLibrary> findByCode(String code);
}

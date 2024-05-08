package org.example.server.repositories;

import org.example.server.models.DefaultLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefaultLibraryRepository extends JpaRepository<DefaultLibrary, Long> {
    Optional<DefaultLibrary> findByTitle(String title);
    Optional<DefaultLibrary> findByCode(String code);
}

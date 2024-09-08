package com.example.JustLifeCaseStudy.Repository;

import com.example.JustLifeCaseStudy.Model.Cleaner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner, String> {

    List<Cleaner> findByIdIn(List<String> cleanerIds);
}


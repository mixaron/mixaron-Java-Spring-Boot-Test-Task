package ru.ryabchuk.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ryabchuk.testtask.models.House;

import java.util.Optional;

@Repository
public interface HouseRepo extends JpaRepository<House, Long> {

}

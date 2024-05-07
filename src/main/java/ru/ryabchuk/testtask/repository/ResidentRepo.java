package ru.ryabchuk.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ryabchuk.testtask.models.HouseResident;
import ru.ryabchuk.testtask.models.Person;

@Repository
public interface ResidentRepo extends JpaRepository<HouseResident, Long> {
    void deleteByResident(Person resident);
}

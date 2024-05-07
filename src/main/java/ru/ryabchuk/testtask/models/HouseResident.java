package ru.ryabchuk.testtask.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "house_residents")
@Data
@NoArgsConstructor
public class HouseResident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Person resident;

    public HouseResident(Person person, House house) {
        this.house = house;
        this.resident = person;
    }
}
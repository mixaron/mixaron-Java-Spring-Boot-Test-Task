package ru.ryabchuk.testtask.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Address cannot be null")
    private String address;


    @OneToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Person owner;

    @OneToMany(mappedBy = "house")
    @JsonIgnore
    private List<HouseResident> residents;

    public House(Long houseId, String address) {
        this.id = houseId;
        this.address = address;
    }
}

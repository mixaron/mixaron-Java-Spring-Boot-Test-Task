package ru.ryabchuk.testtask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
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
}

package org.example;

import javax.persistence.*;

@MappedSuperclass
public abstract class IDProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    public int getId() {
        return id;
    }
}

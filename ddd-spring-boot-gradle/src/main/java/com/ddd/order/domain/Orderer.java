package com.ddd.order.domain;

import java.util.Objects;

public class Orderer {
    private String name;
    private String email;

    public Orderer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderer orderer = (Orderer) o;
        return Objects.equals(name, orderer.name) && Objects.equals(email, orderer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}

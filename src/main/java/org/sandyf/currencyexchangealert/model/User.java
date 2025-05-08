package org.sandyf.currencyexchangealert.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
}
package com.karlglans.whattodo.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Todo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String message;

  private Boolean completed;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;
}

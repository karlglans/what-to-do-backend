package com.karlglans.whattodo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonIgnore
  @ManyToOne
//  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;
}

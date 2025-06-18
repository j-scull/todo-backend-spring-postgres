package com.todo.app.core;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	private String title;

	private boolean completed;

	private int order;

}

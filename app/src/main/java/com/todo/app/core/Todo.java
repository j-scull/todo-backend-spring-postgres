package com.todo.app.core;

import jakarta.persistence.*;
import lombok.*;

// todo - consider renaming this class

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "todos")
public class Todo {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	@NonNull
	private String title;

	@NonNull
	private Boolean completed;

	@NonNull
	private Integer order;

}

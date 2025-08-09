package com.todo.app.core.dto;

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
	private String todoTitle;

	@NonNull
	private Boolean todoIsCompleted;

	@NonNull
	private Integer todoOrder;

}

package com.todo.app.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.net.URI;

@Getter
@Builder
public class TodoResponse {

	private long id;

	private String title;

	private boolean completed;

	private int order;

	private URI uri;

}

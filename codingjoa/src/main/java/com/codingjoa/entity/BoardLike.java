package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	id                  NUMBER,
    board_id            NUMBER      NOT NULL,
	user_id             NUMBER      NOT NULL,
    created_at          DATE        NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class BoardLike {
	
	private Long id;
	private Long boardId;
	private Long userId;
	private LocalDateTime createdAt;
	
}

package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	id              NUMBER,
    reply_id        NUMBER      NOT NULL,
	user_id         NUMBER      NOT NULL,
    created_at      DATE        NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class ReplyLike {
	
	private Long id;
	private Long replyId;
	private Long userId;
	private LocalDateTime createdAt;
	
}

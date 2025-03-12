package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import com.codingjoa.converter.WhitespaceDeserializer;
import com.codingjoa.entity.Reply;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // for deserializing (JSON --> Object)
public class ReplyDto {

	private long id;
	private long boardId;
	private long userId;

	@JsonDeserialize(using = WhitespaceDeserializer.class)
	@NotBlank
	private String content;
	
	private boolean status;
	
	@Builder
	private ReplyDto(long id, long boardId, long userId, @NotBlank String content, boolean status) {
		this.id = id;
		this.boardId = boardId;
		this.userId = userId;
		this.content = content;
		this.status = status;
	}
	
	public Reply toEntity() {
		return Reply.builder()
				.id(this.id)
				.boardId(this.boardId)
				.userId(this.userId)
				.content(this.content)
				.status(this.status)
				.build();
	}
	
	private String escapeContent() {
		return (content != null) ? content.replace("\r\n", "\\r\\n") : null;
	}

	@Override
	public String toString() {
		return "ReplyDto [id=" + id + ", boardId=" + boardId + ", userId=" + userId + ", content=" + escapeContent()
				+ ", status=" + status + "]";
	}
	
}

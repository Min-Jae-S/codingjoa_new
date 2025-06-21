package com.codingjoa.util;

public class RedisKeyUtils {

	public static long extractEntityId(String key) {
		// "board:123:comment_count" --> [ "board", "123", "comment_count" ]
		String[] parts = key.split(":"); 
		return Long.parseLong(parts[1]);
	}
	
	public static String createViewCountKey(Long boardId) {
		return String.format("board:%d:view_count", boardId);
	}
	
	public static String createCommentCountKey(Long boardId) {
		return String.format("board:%d:comment_count", boardId);
	}

	public static String createBoardLikeCountKey(Long boardId) {
		return String.format("board:%d:like_count", boardId);
	}
	
	public static String createCommentLikeCountKey(Long commentId) {
		return String.format("comment:%d:like_count", commentId);
	}
	
	
}

package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.UserImage;

@Mapper
public interface ImageMapper {
	
	boolean insertUserImage(UserImage userImage);
	
	void resetUserImageLatestFlag(Long userId);

	boolean insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(Long imageId);
	
	void activateBoardImages(@Param("boardImages") List<Long> boardImages, @Param("boardId") Long boardId);
	
	void deactivateBoardImages(Long boardId);
	
}

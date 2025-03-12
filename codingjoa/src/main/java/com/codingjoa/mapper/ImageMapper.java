package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.UserImage;

@Mapper
public interface ImageMapper {

	boolean insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(Long boardImageId);
	
	void activateBoardImages(@Param("boardImages") List<Long> boardImages, @Param("boardId") Long boardId);
	
	void deactivateBoardImages(Long boardId);
	
	void deactivateUserImage(Long userId);

	boolean insertUserImage(UserImage userImage);
	
	List<BoardImage> findTempBoardImages();
	
	List<UserImage> findTempUserImages();
	
}

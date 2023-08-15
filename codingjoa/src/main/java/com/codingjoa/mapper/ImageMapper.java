package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.ProfileImage;

@Mapper
public interface ImageMapper {

	void insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(@Param("boardImages") List<Integer> boardImages, @Param("boardIdx") int boardIdx);
	
	void deactivateBoardImage(int boarIdx);
	
	List<BoardImage> findBoardImagesByBoardIdx(int boardIdx);
	
	void deactivateProfileImage(int memberIdx);

	void insertProfileImage(ProfileImage profileImage);
	
	// test
	BoardImage findBoardImageByIdx(Integer boardImageIdx);
	
}

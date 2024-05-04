package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.MemberImage;

@Mapper
public interface ImageMapper {

	void insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(@Param("boardImages") List<Integer> boardImages, @Param("boardIdx") int boardIdx);
	
	void deactivateBoardImages(int boarIdx);
	
	List<BoardImage> findBoardImagesByBoardIdx(int boardIdx);
	
	void deactivateMemberImage(int memberIdx);

	void insertMemberImage(MemberImage memberImage);
	
	// test
	BoardImage findBoardImageByIdx(Integer boardImageIdx);
	
	BoardImage findBoardImageByName(String boardImageName);
	
	MemberImage findMemberImageByName(String memberImageName);
	
	List<BoardImage> findTempBoardImages();
	
	List<MemberImage> findTempMemberImages();
	
}

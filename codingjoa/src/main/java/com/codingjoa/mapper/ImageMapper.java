package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;
import com.codingjoa.entity.MemberImage;

@Mapper
public interface ImageMapper {

	boolean insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImages(@Param("boardImages") List<Integer> boardImages, @Param("boardIdx") int boardIdx);
	
	void deactivateBoardImages(int boardIdx);
	
	void deactivateMemberImage(int memberIdx);

	boolean insertMemberImage(MemberImage memberImage);
	
	List<BoardImage> findTempBoardImages();
	
	List<MemberImage> findTempMemberImages();
	
}

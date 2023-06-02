package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Upload;

@Mapper
public interface UploadMapper {

	int insertUpload(Upload upload);
	
	boolean isImageUploaded(int uploadIdx);
	
	void activateImage(@Param("uploadBoardIdx") int uploadBoardIdx, @Param("uploadIdxList") List<Integer> uploadIdxList);
	
	List<Integer> findUploadIdxList(int uploadBoardIdx);
	
	void deactivateImage(int uploadBoardIdx);
	
}

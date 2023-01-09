package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Upload;

@Mapper
public interface UploadMapper {

	int insertUpload(Upload upload);
	
	boolean isTempImageUploaded(int uploadIdx);
	
	void activateImage(@Param("boardIdx") int boardIdx, @Param("uploadIdxList") List<Integer> uploadIdxList);
	
	List<Integer> findUploadIdxList(int boardIdx);
	
	void deactivateImage(List<Integer> uploadIdxList);
	
}

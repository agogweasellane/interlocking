package com.agogweasellane.interlocking.models.service_layer.external;


import java.util.List;

import org.springframework.stereotype.Service;

import com.agogweasellane.interlocking.controllers.FileDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class FileService extends com.agogweasellane.interlocking.base.service.BaseService<FileDto>
{//TO-DO. 프로젝트마다 클라우드 저장소가 달라질경우를 대비. 컨트롤러에서 실제로 호출할건 이거고, 내부에서는 AWS인지 구글인지로 주석/해제
	@Override
	public boolean insert(boolean isOverWrite, FileDto... argDto)
	{
		return false;
	}

	@Override
	public boolean update(FileDto... argDto) {
		return false;
	}

	@Override
	public boolean update(String argQuery) {
		return false;
	}

	@Override
	public FileDto findItem(FileDto argDto) {
		return null;
	}

	@Override
	public List<FileDto> findItmes(FileDto argDto) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public boolean delete(FileDto... argDto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}
}
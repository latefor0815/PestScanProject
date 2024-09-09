package com.busanit501.pesttestproject0909.service;

import com.busanit501.pesttestproject0909.dto.InsectDto;
import com.busanit501.pesttestproject0909.entity.Insect;
import com.busanit501.pesttestproject0909.repository.InsectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsectService {

    @Autowired
    private InsectRepository insectRepository;

    /**
     * 모든 해충 목록 조회 (InsectDto로 변환하여 반환)
     */
    public List<InsectDto> getAllInsects() {
        List<Insect> insects = insectRepository.findAll();
        return insects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 해충 ID로 해충 정보 조회 (InsectDto로 변환하여 반환)
     */
    public InsectDto getInsectById(Long id) {
        Insect insect = insectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insect not found"));
        return convertToDto(insect);
    }

    /**
     * 새로운 해충 정보 저장 (InsectDto로 받아 저장 후 InsectDto로 반환)
     */
    public InsectDto saveInsect(InsectDto insectDto) {
        Insect insect = convertToEntity(insectDto);
        Insect savedInsect = insectRepository.save(insect);
        return convertToDto(savedInsect);
    }

    // DTO -> Entity 변환
    private Insect convertToEntity(InsectDto insectDto) {
        Insect insect = new Insect();
        insect.setId(insectDto.getId());
        insect.setName(insectDto.getName());
        insect.setSpecies(insectDto.getSpecies());
        insect.setDescription(insectDto.getDescription());
        return insect;
    }

    // Entity -> DTO 변환
    private InsectDto convertToDto(Insect insect) {
        return new InsectDto(
                insect.getId(),
                insect.getName(),
                insect.getSpecies(),
                insect.getDescription()
        );
    }
}
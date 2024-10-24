package com.hexagonal.challenge.aplication.port.in;

import com.hexagonal.challenge.aplication.dto.CreativeDTO;
import com.hexagonal.challenge.aplication.dto.CreativeUpdateDTO;
import com.hexagonal.challenge.domain.Creative;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCreativeUseCase {

    Creative createCreative(@Valid CreativeDTO creativeDTO);

    List<Creative> listCreatives(PageRequest pageRequest);

    Optional<Creative> getCreativeById(String creativeId);

    Optional<Creative> updateCreative(@NotBlank String creativeId, CreativeUpdateDTO creative);

    boolean deleteCreative(String creativeId);
}

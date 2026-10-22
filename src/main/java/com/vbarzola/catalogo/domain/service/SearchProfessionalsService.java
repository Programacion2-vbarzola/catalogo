package com.vbarzola.catalogo.domain.service;

import com.vbarzola.catalogo.domain.dto.ProfessionalDTO;
import com.vbarzola.catalogo.domain.mapper.ProfessionalMapper;
import com.vbarzola.catalogo.domain.port.in.SearchProfessionalsUseCase;
import com.vbarzola.catalogo.domain.port.out.ProfessionalRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SearchProfessionalsService implements SearchProfessionalsUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(SearchProfessionalsService.class);

    private final ProfessionalRepositoryPort repository;
    private final ProfessionalMapper mapper;

    public SearchProfessionalsService(ProfessionalRepositoryPort repository, ProfessionalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<ProfessionalDTO> search(String name, Long categoryId, Boolean enabled, Pageable pageable) {
        LOG.debug("Request to search Professionals: name={}, categoryId={}, enabled={}", name, categoryId, enabled);
        return repository.search(name, categoryId, enabled, pageable).map(mapper::toDto);
    }
}

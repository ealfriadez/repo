package com.example.bookstoreai.service.impl;

import com.example.bookstoreai.dto.request.AuthorRequest;
import com.example.bookstoreai.dto.response.AuthorResponse;
import com.example.bookstoreai.exception.BusinessException;
import com.example.bookstoreai.exception.ResourceNotFoundException;
import com.example.bookstoreai.mapper.AuthorMapper;
import com.example.bookstoreai.model.Author;
import com.example.bookstoreai.repository.AuthorRepository;
import com.example.bookstoreai.repository.BookRepository;
import com.example.bookstoreai.service.IAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorResponse> findAll(Pageable pageable) {
        return findAll(null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthorResponse> findAll(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return authorRepository.searchByName(search, pageable)
                    .map(authorMapper::toResponse);
        }
        return authorRepository.findAll(pageable)
                .map(authorMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse findById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        return authorMapper.toResponse(author);
    }

    @Override
    public AuthorResponse create(AuthorRequest request) {
        validateUniqueAuthor(request.firstName(), request.lastName(), null);
        Author author = authorMapper.toEntity(request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
        validateUniqueAuthor(request.firstName(), request.lastName(), id);
        authorMapper.updateEntityFromRequest(request, author);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author", id);
        }
        if (bookRepository.existsByAuthorId(id)) {
            throw new BusinessException("No se puede eliminar el autor porque tiene libros asociados");
        }
        authorRepository.deleteById(id);
    }

    private void validateUniqueAuthor(String firstName, String lastName, Long excludeId) {
        boolean exists = excludeId == null
                ? authorRepository.existsByFirstNameAndLastName(firstName, lastName)
                : authorRepository.existsByFirstNameAndLastNameAndIdNot(firstName, lastName, excludeId);
        if (exists) {
            throw new BusinessException("Ya existe un autor con el nombre '%s %s'".formatted(firstName, lastName));
        }
    }
}

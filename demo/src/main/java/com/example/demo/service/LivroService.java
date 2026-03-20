package com.example.demo.service;

import com.example.demo.entity.Livro;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmprestimoRepository;
import com.example.demo.repository.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    @Autowired // Injeção de dependência via construtor (preferível)
    public LivroService(LivroRepository livroRepository, EmprestimoRepository emprestimoRepository) {
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Transactional
    public Livro adicionar(Livro livro) {
        return livroRepository.save(livro);
    }

    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscar(Long id) {
        return livroRepository.findById(id);
    }

    @Transactional
    public void remover(Long id) {
        livroRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Livro não encontrado com id: " + id));

        emprestimoRepository.findByLivroIdAndDataDevolucaoRealIsNull(id).ifPresent(e -> {
            throw new IllegalStateException("Não é possível remover um livro que está atualmente emprestado.");
        });
        livroRepository.deleteById(id);
    }

    @Transactional
    public Optional<Livro> atualizar(Long id, Livro livroAtualizado) {
        return livroRepository.findById(id).map(livroExistente -> {
            livroExistente.setTitulo(livroAtualizado.getTitulo());
            livroExistente.setAutor(livroAtualizado.getAutor());
            return livroRepository.save(livroExistente);
        });
    }
}
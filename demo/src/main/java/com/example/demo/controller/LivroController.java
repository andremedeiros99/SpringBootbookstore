package com.example.demo.controller;

import com.example.demo.entity.Livro;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    @Autowired
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public List<Livro> listar() {
        return livroService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscar(@PathVariable Long id) {
        return livroService.buscar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Livro> adicionar(@RequestBody Livro livro) {
        // Validações básicas de payload podem ser adicionadas aqui ou na camada de serviço
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty() ||
                livro.getAutor() == null || livro.getAutor().trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Ou lançar uma exceção customizada
        }
        Livro novoLivro = livroService.adicionar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id, @RequestBody Livro livroAtualizado) {
        if (livroAtualizado.getTitulo() == null || livroAtualizado.getTitulo().trim().isEmpty() ||
                livroAtualizado.getAutor() == null || livroAtualizado.getAutor().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return livroService.atualizar(id, livroAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            livroService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            // CONFLICT (409) se o livro estiver emprestado
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
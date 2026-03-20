package com.example.demo.controller;

import com.example.demo.dto.DevolucaoRequestDTO;
import com.example.demo.dto.EmprestimoRequestDTO;
import com.example.demo.entity.Emprestimo;
import com.example.demo.entity.Livro;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.EmprestimoService;
import com.example.demo.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @Autowired
    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping("/emprestar")
    public ResponseEntity<?> emprestar(@RequestBody EmprestimoRequestDTO request) {
        if (request.getLivroId() == null || request.getUsuarioId() == null) {
            return ResponseEntity.badRequest().body("livroId e usuarioId são obrigatórios.");
        }
        try {
            Emprestimo emprestimo = emprestimoService.emprestarLivro(request.getLivroId(), request.getUsuarioId());
            return ResponseEntity.status(HttpStatus.CREATED).body(emprestimo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/devolver")
    public ResponseEntity<?> devolver(@RequestBody DevolucaoRequestDTO request) {
        if (request.getLivroId() == null) {
            return ResponseEntity.badRequest().body("livroId é obrigatório.");
        }
        try {
            Emprestimo emprestimo = emprestimoService.devolverLivro(request.getLivroId());
            return ResponseEntity.ok(emprestimo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/ativos")
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoService.listarEmprestimosAtivos();
    }
}

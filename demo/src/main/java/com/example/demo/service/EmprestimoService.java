package com.example.demo.service;

import com.example.demo.entity.Emprestimo;
import com.example.demo.entity.Livro;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmprestimoRepository;
import com.example.demo.repository.LivroRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Emprestimo emprestarLivro(Long livroId, Long usuarioId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new ResourceNotFoundException("Livro com ID " + livroId + " não encontrado."));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + usuarioId + " não encontrado."));

        emprestimoRepository.findByLivroIdAndDataDevolucaoRealIsNull(livroId).ifPresent(e -> {
            throw new IllegalStateException("Livro '" + livro.getTitulo() + "' já está emprestado.");
        });

        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setLivro(livro);
        novoEmprestimo.setUsuario(usuario);
        novoEmprestimo.setDataEmprestimo(LocalDate.now());
        novoEmprestimo.setDataDevolucaoPrevista(LocalDate.now().plusWeeks(2));

        return emprestimoRepository.save(novoEmprestimo);
    }

    @Transactional
    public Emprestimo devolverLivro(Long livroId) {
        Emprestimo emprestimoAtivo = emprestimoRepository.findByLivroIdAndDataDevolucaoRealIsNull(livroId)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum empréstimo ativo encontrado para o livro com ID " + livroId + "."));

        emprestimoAtivo.setDataDevolucaoReal(LocalDate.now());
        return emprestimoRepository.save(emprestimoAtivo);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findByDataDevolucaoRealIsNull();
    }
}
package com.example.demo.service;

import com.example.demo.entity.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario adicionar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscar(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public void remover(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Usuário não encontrado com id: " + id));
        if(!usuario.getEmprestimos().isEmpty() && usuario.getEmprestimos().stream().anyMatch(e -> e.getDataDevolucaoReal() == null)){
            throw new IllegalStateException("Usuario possui empréstimos ativos.");
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public Optional<Usuario> atualizar(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            return usuarioRepository.save(usuarioExistente);
        });
    }
}

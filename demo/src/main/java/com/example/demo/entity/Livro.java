package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "livro_seq_generator")
    @SequenceGenerator(name = "livro_seq_generator", sequenceName = "LIVRO_ID_SEQ", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String autor;

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Emprestimo> historicoEmprestimos = new ArrayList<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public List<Emprestimo> getHistoricoEmprestimos() { return historicoEmprestimos; }
    public void setHistoricoEmprestimos(List<Emprestimo> historicoEmprestimos) { this.historicoEmprestimos = historicoEmprestimos; }
}

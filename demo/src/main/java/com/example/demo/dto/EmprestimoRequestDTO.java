package com.example.demo.dto;

public class EmprestimoRequestDTO {
    private Long livroId;
    private Long usuarioId;

    // Getters e Setters (ou usar Lombok @Data)
    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}

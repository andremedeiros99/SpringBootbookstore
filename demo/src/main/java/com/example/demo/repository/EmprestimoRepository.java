package com.example.demo.repository;

import com.example.demo.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Optional<Emprestimo> findByLivroIdAndDataDevolucaoRealIsNull(Long livroId);
    List<Emprestimo> findByDataDevolucaoRealIsNull();
}
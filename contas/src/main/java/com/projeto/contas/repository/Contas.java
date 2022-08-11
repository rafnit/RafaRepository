package com.projeto.contas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.contas.model.Conta;

public interface Contas extends JpaRepository<Conta, Integer> {
	
}
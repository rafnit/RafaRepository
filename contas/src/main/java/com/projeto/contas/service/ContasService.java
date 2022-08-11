package com.projeto.contas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.projeto.contas.model.Conta;
import com.projeto.contas.repository.Contas;

@Service
public class ContasService {
	
	@Autowired
	private Contas contas;
	
	public List<Conta> pesquisar() {
		return contas.findAll();
	}
	
	
	public void salvar(Conta conta) {
		try {
			contas.save(conta);
			
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato do campo de tipo DATA inválido");
		}
	}
	
	
	public void excluir(Integer codigo) {
		contas.deleteById(codigo);
	}
	
	
	public void executar(Integer codigo) {
		Conta conta = contas.findById(codigo).get();
		conta.setExecutado(true);
		contas.save(conta);
	}
}
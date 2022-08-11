package com.projeto.contas.model.enums;

public enum TipoConta {
	RECEITA("Receita"),
	DESPESA("Despesa");
	
	private String descricao;
	
	TipoConta(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}
}
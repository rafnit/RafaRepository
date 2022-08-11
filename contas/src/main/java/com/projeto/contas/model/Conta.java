package com.projeto.contas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.projeto.contas.converter.LocalDateConverter;
import com.projeto.contas.model.enums.TipoConta;


// ENTIDADE JPA
@Entity
@Table(name = "CONTAS")
public class Conta implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigo;
	
	@NotEmpty(message = "Descrição é obrigatória")
	@Length(max = 50)
	private String descricao;
	
	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.01", message = "Valor deve ser maior que 0,01")
	@DecimalMax(value = "999999.99", message = "Valor deve ser menor que 999.999,99")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valor;
	
	// EnumType.ORDINAL - SALVA NO BANCO COM NUMERAL
	// EnumType.STRING - SALVA A LABEL (STRING) DO ENUM
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Tipo é obrigatório")
	private TipoConta tipo;
	
	@Column
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Convert(converter = LocalDateConverter.class)
	private LocalDate dataDebito;
	
	private Boolean executado;
	
	
	public boolean isReceita() {
		return TipoConta.RECEITA.equals(this.tipo);
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public TipoConta getTipo() {
		return tipo;
	}
	public void setTipo(TipoConta tipo) {
		this.tipo = tipo;
	}
	public Boolean getExecutado() {
		return executado;
	}
	public void setExecutado(Boolean executado) {
		this.executado = executado;
	}
	public LocalDate getDataDebito() {
		return dataDebito;
	}
	public void setDataDebito(LocalDate dataDebito) {
		this.dataDebito = dataDebito;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conta other = (Conta) obj;
		return Objects.equals(codigo, other.codigo);
	}
}
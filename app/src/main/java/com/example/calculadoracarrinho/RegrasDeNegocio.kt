package com.example.calculadoracarrinho

import java.text.NumberFormat
import java.util.Locale

// Camada de domínio: funções puras.
// Elas dependem apenas dos parâmetros recebidos e não alteram nada fora delas,
// então a mesma entrada sempre gera a mesma saída.

private val localeBrasil: Locale = Locale.forLanguageTag("pt-BR")

// ---------- Cálculos de um item ----------

/** Preço x quantidade, sem desconto. */
fun calcularSubtotalBruto(preco: Double, quantidade: Int): Double =
    preco * quantidade

/** Valor em reais do desconto aplicado sobre o subtotal bruto. */
fun calcularValorDesconto(preco: Double, quantidade: Int, descontoPercentual: Double): Double =
    calcularSubtotalBruto(preco, quantidade) * descontoPercentual / 100

/** Valor final do item: subtotal bruto menos o desconto (quando existir). */
fun calcularValorFinal(preco: Double, quantidade: Int, descontoPercentual: Double): Double =
    calcularSubtotalBruto(preco, quantidade) -
        calcularValorDesconto(preco, quantidade, descontoPercentual)

// ---------- Cálculos do carrinho ----------

/** Soma dos preços sem desconto. */
fun calcularSubtotalDoCarrinho(itens: List<ItemCarrinho>): Double =
    itens.sumOf { calcularSubtotalBruto(it.produto.preco, it.quantidade) }

/** Soma de todos os descontos aplicados. */
fun calcularDescontosDoCarrinho(itens: List<ItemCarrinho>): Double =
    itens.sumOf {
        calcularValorDesconto(it.produto.preco, it.quantidade, it.produto.descontoPercentual)
    }

/** Valor total final: soma dos totais de cada item, já com desconto. */
fun calcularTotalDoCarrinho(itens: List<ItemCarrinho>): Double =
    itens.sumOf { it.calcularTotal() }

// ---------- Formatação ----------

/** Formata um valor em reais, por exemplo: R$ 3.499,00. */
fun formatarMoeda(valor: Double): String =
    NumberFormat.getCurrencyInstance(localeBrasil).format(valor)

/** Formata um percentual sem casas decimais desnecessárias, por exemplo: 5%. */
fun formatarPercentual(valor: Double): String {
    val formatador = NumberFormat.getNumberInstance(localeBrasil)
    formatador.maximumFractionDigits = 2
    return formatador.format(valor) + "%"
}

// ---------- Relatório (processamento de coleções) ----------

/**
 * Gera as linhas do relatório de produtos com desconto.
 *
 * filter  -> mantém apenas os itens que tiveram desconto aplicado
 * map     -> transforma cada item em um par (nome, valor final)
 * sortedByDescending -> ordena do maior valor final para o menor
 * fold    -> reduz a lista a um único valor (a soma dos totais)
 */
fun gerarRelatorioDeDescontos(itens: List<ItemCarrinho>): List<String> {
    val itensComDesconto = itens
        .filter { it.produto.descontoPercentual > 0 }
        .map { it.produto.nome to it.calcularTotal() }
        .sortedByDescending { (_, valorFinal) -> valorFinal }

    val linhasDosProdutos = itensComDesconto.mapIndexed { indice, (nome, valorFinal) ->
        "${indice + 1}. $nome - ${formatarMoeda(valorFinal)}"
    }

    val somaDosTotais = itensComDesconto.fold(0.0) { acumulado, (_, valorFinal) ->
        acumulado + valorFinal
    }

    return listOf("===== PRODUTOS COM DESCONTO APLICADO =====") +
        linhasDosProdutos +
        "Total dos produtos com desconto: ${formatarMoeda(somaDosTotais)}"
}

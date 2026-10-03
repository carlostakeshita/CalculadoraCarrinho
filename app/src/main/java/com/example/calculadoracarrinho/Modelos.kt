package com.example.calculadoracarrinho

/**
 * Contrato para qualquer entidade que possua um valor total a pagar.
 */
interface Pagavel {
    fun calcularTotal(): Double
}

/**
 * Produto do catálogo.
 * A descrição pode não existir (nula) e o desconto percentual é zero por padrão.
 */
data class Produto(
    val nome: String,
    val preco: Double,
    val descricao: String? = null,
    val descontoPercentual: Double = 0.0
)

/**
 * Item do carrinho: relaciona um produto a uma quantidade.
 * O total do item já considera o desconto do produto.
 */
data class ItemCarrinho(
    val produto: Produto,
    val quantidade: Int
) : Pagavel {
    override fun calcularTotal(): Double =
        calcularValorFinal(produto.preco, quantidade, produto.descontoPercentual)
}

/**
 * Carrinho de compras: conjunto de itens.
 * O total do carrinho é a soma dos totais de cada item.
 */
data class Carrinho(
    val itens: List<ItemCarrinho>
) : Pagavel {
    override fun calcularTotal(): Double = calcularTotalDoCarrinho(itens)
}

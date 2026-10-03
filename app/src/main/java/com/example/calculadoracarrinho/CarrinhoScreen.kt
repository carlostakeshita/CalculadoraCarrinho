package com.example.calculadoracarrinho

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Tela principal: cabeçalho, lista de itens do carrinho e resumo com os totais.
 */
@Composable
fun TelaCarrinho(carrinho: Carrinho) {
    Scaffold(
        topBar = { CabecalhoCarrinho(titulo = "🛒 Meu Carrinho") },
        bottomBar = {
            ResumoCarrinho(
                subtotal = calcularSubtotalDoCarrinho(carrinho.itens),
                descontos = calcularDescontosDoCarrinho(carrinho.itens),
                total = carrinho.calcularTotal()
            )
        }
    ) { espacoInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacoInterno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Laço de repetição: uma LinhaProduto para cada item do carrinho.
            carrinho.itens.forEach { item ->
                LinhaProduto(
                    nome = item.produto.nome,
                    descricao = item.produto.descricao,
                    precoUnitario = item.produto.preco,
                    quantidade = item.quantidade,
                    totalBruto = calcularSubtotalBruto(item.produto.preco, item.quantidade),
                    descontoPercentual = item.produto.descontoPercentual,
                    valorDesconto = calcularValorDesconto(
                        item.produto.preco,
                        item.quantidade,
                        item.produto.descontoPercentual
                    ),
                    totalFinal = item.calcularTotal()
                )
            }
        }
    }
}

@Composable
fun CabecalhoCarrinho(titulo: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        )
    }
}

/**
 * Componente reutilizável da linha do produto.
 * Tudo o que aparece vem por parâmetro: não há dado de produto fixo aqui dentro.
 */
@Composable
fun LinhaProduto(
    nome: String,
    descricao: String?,
    precoUnitario: Double,
    quantidade: Int,
    totalBruto: Double,
    descontoPercentual: Double,
    valorDesconto: Double,
    totalFinal: Double,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Nome: no máximo 1 linha, com reticências se passar.
            Text(
                text = nome,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Descrição: no máximo 2 linhas. Se for nula, o operador ?: usa o texto padrão.
            Text(
                text = descricao ?: "Sem descrição",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Preço unitário, quantidade e total calculado.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatarMoeda(precoUnitario),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "x$quantidade",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = formatarMoeda(totalBruto),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Só aparece quando o produto tem desconto.
            if (descontoPercentual > 0) {
                LinhaRotuloValor(
                    rotulo = "Desconto de ${formatarPercentual(descontoPercentual)}",
                    valor = "-${formatarMoeda(valorDesconto)}",
                    estilo = MaterialTheme.typography.labelLarge
                )
                LinhaRotuloValor(
                    rotulo = "Total com desconto",
                    valor = formatarMoeda(totalFinal),
                    estilo = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * Resumo com os valores consolidados do carrinho.
 */
@Composable
fun ResumoCarrinho(subtotal: Double, descontos: Double, total: Double) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinhaRotuloValor(
                rotulo = "Subtotal",
                valor = formatarMoeda(subtotal),
                estilo = MaterialTheme.typography.bodyLarge
            )
            LinhaRotuloValor(
                rotulo = "Descontos",
                valor = "-${formatarMoeda(descontos)}",
                estilo = MaterialTheme.typography.bodyLarge
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
            LinhaRotuloValor(
                rotulo = "TOTAL",
                valor = formatarMoeda(total),
                estilo = MaterialTheme.typography.titleLarge
            )
        }
    }
}

/**
 * Linha simples com um rótulo à esquerda e um valor à direita.
 */
@Composable
fun LinhaRotuloValor(rotulo: String, valor: String, estilo: TextStyle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = rotulo, style = estilo)
        Text(text = valor, style = estilo)
    }
}

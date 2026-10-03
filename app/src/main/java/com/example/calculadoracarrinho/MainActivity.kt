package com.example.calculadoracarrinho

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

private const val TAG_RELATORIO = "CarrinhoRelatorio"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val carrinho = Catalogo.carrinho

        // Relatório no Logcat (filtre pela tag "CarrinhoRelatorio").
        // Fica fora do setContent para ser impresso uma única vez.
        gerarRelatorioDeDescontos(carrinho.itens).forEach { linha ->
            Log.i(TAG_RELATORIO, linha)
        }

        setContent {
            val cores = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
            MaterialTheme(colorScheme = cores) {
                TelaCarrinho(carrinho = carrinho)
            }
        }
    }
}

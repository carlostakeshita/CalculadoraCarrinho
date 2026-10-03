package com.example.calculadoracarrinho

/**
 * Catálogo fixo de produtos e o carrinho usado no cenário de validação.
 */
object Catalogo {

    // Produto com desconto e descrição longa (testa o limite de 2 linhas).
    private val notebook = Produto(
        nome = "Notebook Dell Inspiron",
        preco = 3499.00,
        descricao = "Um notebook rápido para trabalho e estudo, com processador Intel Core i5, " +
            "16 GB de memória RAM, SSD de 512 GB e tela Full HD de 15,6 polegadas.",
        descontoPercentual = 5.0
    )

    // Produto sem descrição (valor nulo) e sem desconto (valor padrão).
    private val mouse = Produto(
        nome = "Mouse sem fio",
        preco = 89.90
    )

    // Produto com nome longo (testa o limite de 1 linha com reticências).
    private val teclado = Produto(
        nome = "Teclado mecânico RGB com switch azul, layout ABNT2 e iluminação personalizável",
        preco = 349.90,
        descricao = "Switch azul, ABNT2"
    )

    /** Lista fixa com todos os produtos disponíveis. */
    val produtos: List<Produto> = listOf(
        notebook,
        mouse,
        teclado,
        Produto(
            nome = "Monitor UltraWide 29 polegadas",
            preco = 1299.00,
            descricao = "Painel IPS Full HD com taxa de atualização de 75 Hz",
            descontoPercentual = 10.0
        ),
        Produto(
            nome = "Headset gamer com microfone",
            preco = 249.90,
            descricao = "Som estéreo e microfone removível",
            descontoPercentual = 15.0
        ),
        Produto(
            nome = "Webcam Full HD",
            preco = 199.90
        ),
        Produto(
            nome = "Cabo HDMI 2.1 de 2 metros",
            preco = 39.90,
            descricao = "Suporta resolução 4K a 120 Hz"
        )
    )

    /** Carrinho do cenário de validação: 2 notebooks, 1 mouse e 1 teclado. */
    val carrinho: Carrinho = Carrinho(
        itens = listOf(
            ItemCarrinho(produto = notebook, quantidade = 2),
            ItemCarrinho(produto = mouse, quantidade = 1),
            ItemCarrinho(produto = teclado, quantidade = 1)
        )
    )
}

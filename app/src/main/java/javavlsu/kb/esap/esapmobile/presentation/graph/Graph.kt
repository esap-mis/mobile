package javavlsu.kb.esap.esapmobile.presentation.graph

sealed class Graph(
    val root: String
) {
    object Root : Graph("root_graph")

    object Auth : Graph("auth_graph")

    object Main : Graph("main_graph")
}
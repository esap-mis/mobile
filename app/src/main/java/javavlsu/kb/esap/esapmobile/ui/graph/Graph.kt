package javavlsu.kb.esap.esapmobile.ui.graph

sealed class Graph(
    val root: String
) {
    object Auth : Graph("auth_graph")

    object Main : Graph("main_graph")
}
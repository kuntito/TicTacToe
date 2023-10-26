package com.example.tictactoe23.models

enum class LineType(val desc: String) {
    Vertical("vertical line"),
    Horizontal("horizontal line"),
    /**
     * Forward slanting diagonal
     * */
    FSD("forward slanting diagonal"),
    /**
     * Backward slanting diagonal
     * */
    BSD("backward slanting diagonal")
}

data class BoardLine(
    val lineType: LineType,
    val cells: MutableList<CellPosition> = mutableListOf()
) {
    override fun toString(): String {
        return """${lineType.desc} - ${cells.map { "${ it.rowIndex },${it.columnIndex}"}}"""
    }
}

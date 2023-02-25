package com.tito.tictactoe.ui.util

import android.util.Log
import com.tito.tictactoe.models.*
import com.tito.tictactoe.ui.util.UtilityData.CellValue
import java.util.Collections.max

private const val aiTag = "aiTag"
private var aiSelectionAndReason = ""
fun aiSelectCell(
    cellValues: List<List<CellValue>>,
    aiAvatar: Avatar,
    opponentAvatar: Avatar,
    consecAvatarsToWin: Int
){
    if (consecAvatarsToWin < 3){
        throw Exception("consecAvatarsToWin=$consecAvatarsToWin, it should be at least 3")
    }

    val vacantCells = getVacantCells(cellValues.flatten())
    var selectedCell: CellValue? = null


    val cellValuesPerLine = getCellValuesPerLine(cellValues).shuffled()
    val avatarFrequencyPerLine = cellValuesPerLine.map {
        it.getCellsWithAvatar(aiAvatar.drawableRes).size
    }
    when(val maxAvatarFreqPerLine = max(avatarFrequencyPerLine)) {
        0 -> {
            Log.d(aiTag, "AI highestAvatarsOnALine=$maxAvatarFreqPerLine")
            selectedCell = selectAnyVacantCell(vacantCells, cellValues, middleCellBias = true)
        }
        else -> {
            Log.d(aiTag, "AI highestAvatarsOnALine=$maxAvatarFreqPerLine")
            val canAiWinWithNextTurn = canAvatarWinWithNextTurn(
                avatar = aiAvatar,
                cellValuesPerLine = cellValuesPerLine,
                consecAvatarsToWin = consecAvatarsToWin
            )
            val canOpponentWinWithNextTurn = canAvatarWinWithNextTurn(
                avatar = opponentAvatar,
                cellValuesPerLine = cellValuesPerLine,
                consecAvatarsToWin = consecAvatarsToWin
            )
            val canOpponentCreateMultiWays = canAvatarCreateMultipleWays(
                avatar = opponentAvatar,
                cellValuesPerLine = cellValuesPerLine,
                consecAvatarsToWin = consecAvatarsToWin
            )
            if (canAiWinWithNextTurn.winnerCanEmerge) {
                selectedCell = canAiWinWithNextTurn.cell!!
                aiSelectionAndReason = "cell=${selectedCell.id} to win"
            }else if(canOpponentWinWithNextTurn.winnerCanEmerge && getBias()){
                selectedCell = canOpponentWinWithNextTurn.cell!!
                aiSelectionAndReason = "cell=${selectedCell.id} to block opponent win"
            }else if(canOpponentCreateMultiWays.exists
                && canOpponentCreateMultiWays.blockingCells.isNotEmpty() && getBias(freq=8)){
                selectedCell = canOpponentCreateMultiWays.blockingCells.random()
                aiSelectionAndReason = "cell=${selectedCell.id} to block multi ways"
            }else {
                val cellsWithAiAvatar = cellValues.flatten().filter {
                    it.avatarDrawableRes == aiAvatar.drawableRes
                }
                getMostAppropriateCellToSelectAdjacent(
                    cellsWithAvatar = cellsWithAiAvatar,
                    allCellValues = cellValues
                )?.let { cellAndAdjacent ->
                    selectMostAppropriateAdjacent(
                        cellAndAdjacent,
                        cellValuesPerLine,
                        consecAvatarsToWin,
                        opponentAvatar
                    )?.let {
                        selectedCell = it
                        aiSelectionAndReason = "cell=${selectedCell!!.id}, most appropriate adjacent"
                    }
                }
            }
        }
    }

    if (selectedCell == null){
        selectedCell = selectAnyVacantCell(vacantCells, cellValues)
    }

    if (aiSelectionAndReason.isNotEmpty()){
        Log.d(aiTag, "ai selected $aiSelectionAndReason")
        aiSelectionAndReason = ""
    }

    selectedCell?.let {
        it.selectedByAI.value = true
    }
}

/**returns freq <= a random int between [range], if freq is greater than range, it returns false*/
fun getBias(freq: Int = 9, range: IntRange = 1..10): Boolean {
    val randomInt = range.random()
    Log.d(aiTag, "Bias, randomInt=$randomInt")
    if (freq in range){
        val res = randomInt <= freq
        Log.d(aiTag, "Bias, res=$res")
        return res
    }
    return false
}


fun getVacantCells(cellValues: List<CellValue>): List<CellValue>{
//    return cellValues.filter { !it.avatarPlaced }
    return cellValues.filter { it.avatarDrawableRes == null }
}

fun canAvatarWinWithNextTurn(
    avatar: Avatar,
    cellValuesPerLine: List<CellValueLine>,
    consecAvatarsToWin: Int
): CanWinnerEmergeResult {
    for (line in cellValuesPerLine){
        val canWinnerEmergeNextTurn = canWinnerEmergeOnLineWithOneTurn(
            line,
            avatar,
            consecAvatarsToWin
        )
        if (canWinnerEmergeNextTurn.winnerCanEmerge){
            return canWinnerEmergeNextTurn
        }
    }
    return CanWinnerEmergeResult()
}

fun canWinnerEmergeOnLineWithOneTurn(
    line: CellValueLine,
    avatar: Avatar,
    consecAvatarsToWin: Int
): CanWinnerEmergeResult {
    for (index in line.cellValuesList.indices){
        val subListLastIndex = index + consecAvatarsToWin - 1
        if (subListLastIndex in line.cellValuesList.indices){
            // subList is the minimum number of cells a winner can emerge from
            val subList =  line.cellValuesList.subList(index, subListLastIndex+1).toMutableList()
            val vacantCells = getVacantCells(subList)
            if (vacantCells.size == 1){
                val singleVacantCell = vacantCells[0]
                subList.remove(singleVacantCell)
                if (subList.all { it.avatarDrawableRes == avatar.drawableRes }){
                    Log.d(aiTag, "avatarDrawableRes=${avatar.avatarType} can " +
                            "win if next turn played in ${singleVacantCell.id}")
                    return CanWinnerEmergeResult(
                        winnerCanEmerge = true,
                        avatarDrawableRes = avatar.drawableRes,
                        cell = singleVacantCell
                    )
                }
            }
        }else{
            break
        }
    }

    return CanWinnerEmergeResult()
}

/**multiple ways is a scenario where there are at least 2 cells where avatar can win with it's
 * next turn i.e on a 6x6 board where consecAvatarsToWin=4,
 * if an avatar has 3 (consecAvatarsToWin - 1) with a vacant cell on either side like
 * _XXX_, the opponent can only block one win opportunity and hence, a win is guaranteed.
 * On a 3x3 board, where it takes consecAvatarsToWin=3, this would not work as there can't be
 * two vacant cells on either side of 2 consecutive cells(consecAvatarsToWin-1). Although multiple
 * ways is still possible, this function focuses on the identifying the pattern above
 * */
fun canAvatarCreateMultipleWays(
    avatar: Avatar,
    cellValuesPerLine: List<CellValueLine>,
    consecAvatarsToWin: Int
): MultiWaysCheckResult {
    for (line in cellValuesPerLine){
        canMultipleWaysBeCreatedOnLine(avatar, line, consecAvatarsToWin).let { result ->
            if (result.exists){
                return result
            }
        }
    }
    return MultiWaysCheckResult()
}

/**
 * One way multiple ways can exist is when avatar has [consecAvatarsToWin]-1 avatars on a line
 * and has two vacant cells on either side. [consecAvatarsToWin]-2 is a precursor to this scenario
 * so it is to be identified and the cell(s) where opponent avatar can play to block it should be
 * returned
 * */
fun canMultipleWaysBeCreatedOnLine(
    avatar: Avatar,
    line: CellValueLine,
    consecAvatarsToWin: Int
): MultiWaysCheckResult{
    val emergingMultiWays = getEmergingMultiWaysList(consecAvatarsToWin, line.cellValuesList)
    for (list in emergingMultiWays){
        val nonTerminatingCellValues = getNonTerminatingCellValues(list)
        val nonTerminatingCellsWithAvatar = getCellsWithAvatar(nonTerminatingCellValues, avatar)
        val nonTerminatingVacantCells = getVacantCells(nonTerminatingCellValues)

        if (areTerminatingCellsVacant(list)
            && nonTerminatingCellsWithAvatar.size == consecAvatarsToWin-2
            && nonTerminatingVacantCells.isNotEmpty()
        ){
            Log.d(aiTag, "multi ways can be created on ${line.lineType}," +
                    " cells=${list.map { it.id }} if ${avatar.avatarType} " +
                    "plays in cell(s) ${nonTerminatingVacantCells.map { it.id }}")
            return MultiWaysCheckResult(
                exists = true,
                blockingCells = nonTerminatingVacantCells
            )
        }
    }

    return MultiWaysCheckResult()
}

fun getCellsWithAvatar(cellValues: List<CellValue>, avatar: Avatar): List<CellValue>{
    return cellValues.filter { it.avatarDrawableRes == avatar.drawableRes }
}

fun getEmergingMultiWaysList(
    consecAvatarsToWin: Int,
    cellValues: List<CellValue>
): List<List<CellValue>>{
    val emergingMultiWays = mutableListOf<List<CellValue>>()
    if (cellValues.size >= consecAvatarsToWin){
        for (index in cellValues.indices){
            // multiLastIndex =
            val multiLastIndex = index + consecAvatarsToWin
            if (multiLastIndex in cellValues.indices){
                emergingMultiWays.add(
                    cellValues.subList(index, multiLastIndex+1)
                )
            }
        }
    }
    return emergingMultiWays
}

fun areTerminatingCellsVacant(cellValues: List<CellValue>): Boolean{
    return cellValues.let {
        it.first().avatarDrawableRes == null && it.last().avatarDrawableRes == null
    }
}

fun getNonTerminatingCellValues(cellValues: List<CellValue>): List<CellValue>{
    return cellValues.let {
        it.subList(1, it.lastIndex)
    }
}

/**The most appropriate cell to select adjacent is the one with the the most cell avatars and
 * the most vacant cells around it
 * */
fun getMostAppropriateCellToSelectAdjacent(
    cellsWithAvatar: List<CellValue>,
    allCellValues: List<List<CellValue>>
): CellAndAdjacents? {
    if (cellsWithAvatar.isEmpty()) { return null }
    val avatarDrawable = cellsWithAvatar[0].avatarDrawableRes!!
    if (cellsWithAvatar.all { it.avatarDrawableRes != avatarDrawable }){
        throw Exception("cellsWithAvatar must all have the same avatarDrawableRes")
    }

    val selectedCellAndAdjacent = cellsWithAvatar.map {
        getCellAndAdjacents(it, allCellValues)
    }.filter {
        it.vacantAdjacentCells.isNotEmpty()
    }.sortedByDescending {
        it.vacantAdjacentCells.size
    }.sortedByDescending {
        it.adjacentCellsOccupiedByCellAvatar.size
    }[0]

    Log.d(aiTag, "most appropriate cell to select an adjacent is ${selectedCellAndAdjacent.cell.id}")

    return selectedCellAndAdjacent
}

fun getCellAndAdjacents(
    cell: CellValue,
    allCellValues: List<List<CellValue>>
): CellAndAdjacents{
    return CellAndAdjacents(
        cell = cell,
        adjacentCells = getAdjacentCells(cell, allCellValues)
    )
}

fun getAdjacentCells(
    cell: CellValue,
    cellValues: List<List<CellValue>>
): List<CellValue>{
    val targetRowIndex = cell.rowIndex
    val targetColumnIndex = cell.columnIndex
    val rowIndices = cellValues.indices
    val columnIndices = cellValues[0].indices
    val adjacentCells = mutableListOf<CellValue>()

    for (rowIndex in targetRowIndex-1..targetRowIndex+1){
        for(columnIndex in targetColumnIndex-1..targetColumnIndex+1){
            if (rowIndex == targetRowIndex && columnIndex == targetColumnIndex){
                continue
            }
            if (rowIndex in rowIndices && columnIndex in columnIndices){
                adjacentCells.add(cellValues[rowIndex][columnIndex])
            }
        }
    }

    return adjacentCells
}

/**
 * The most appropriate adjacent vacant cell would be the one whose line has the most consecutive
 *  vacant cells and cell avatar
 *  */
fun selectMostAppropriateAdjacent(
    cellAndAdjacent: CellAndAdjacents,
    cellValuesPerLine: List<CellValueLine>,
    consecAvatarsToWin: Int,
    opponentAvatar: Avatar
): CellValue? {
    for (adjacentCell in cellAndAdjacent.vacantAdjacentCells){
        val linesWithAdjacentCell = getLinesWithCell(adjacentCell, cellValuesPerLine)
        for (line in linesWithAdjacentCell){
            for (index in line.cellValuesList.indices) {
                val subListLastIndex = index + consecAvatarsToWin - 1
                if (subListLastIndex in line.cellValuesList.indices){
                    val subList = line.cellValuesList.subList(index, subListLastIndex+1)
                    if (adjacentCell in subList
                        && subList.all { it.avatarDrawableRes != opponentAvatar.drawableRes }){
                        Log.d(aiTag, "most appropriate adjacent cell=${adjacentCell.id}," +
                                " a winner can emerge on ${line.lineType}")
                        return adjacentCell
                    }
                }else{
                    break
                }
            }
        }
    }
    return null
}

fun getLinesWithCell(
    cell: CellValue,
    cellValuesPerLine: List<CellValueLine>
) = cellValuesPerLine.filter {
    cell in it.cellValuesList
}

fun selectAnyVacantCell(
    vacantCells: List<CellValue>,
    cellValues: List<List<CellValue>>,
    /**
     * if [middleCellBias] is true it preferentially selects the middle cell with a hardcoded
     * frequency
     * */
    middleCellBias: Boolean = false
): CellValue{
    if (middleCellBias && getBias()){
        val middleCell = getMiddleCell(cellValues)
        if (middleCell in vacantCells){
            Log.d(aiTag, "ai selected cell=${middleCell.id}, middle cell bias")
            return middleCell
        }
    }
    val randomCell = vacantCells.random()
    Log.d(aiTag, "ai selected cell=${randomCell.id} randomly")
    return randomCell
}

fun getMiddleCell(
    cellValues: List<List<CellValue>>
): CellValue {
    val rowsPerColumn = cellValues.size
    val columnsPerRow = cellValues[0].size

    val middleRow = rowsPerColumn.customMedian()
    val middleColumn = columnsPerRow.customMedian()
    return cellValues[middleRow][middleColumn]
}

fun Int.customMedian(): Int{
    return if (this > 0){
        if (this%2 == 0){
            listOf(
                this/2,
                (this/2)-1
            ).random()
        }else{
            this/2
        }
    }else{
        0
    }
}

fun getCellValuesPerLine(
    cellValues: List<List<CellValue>>
): List<CellValueLine>{
    val cellValuesPerLine = mutableListOf<CellValueLine>()
    val rowIndices = cellValues.indices
    val columnIndices = cellValues[0].indices

    val verticalCellValues = getVerticalCellValues(
        rowIndices = rowIndices,
        columnIndices = columnIndices,
        cellValues = cellValues)
    val horizontalCellValues = getHorizontalCellValues(
        cellValues = cellValues)
    val fsdCellValues = getFsdCellValues(
        rowIndices = rowIndices,
        columnIndices = columnIndices,
        cellValues = cellValues)
    val bsdCellValues = getBsdCellValues(
        rowIndices = rowIndices,
        columnIndices = columnIndices,
        cellValues = cellValues)


    cellValuesPerLine.addAll(horizontalCellValues)
    cellValuesPerLine.addAll(verticalCellValues)
    cellValuesPerLine.addAll(fsdCellValues)
    cellValuesPerLine.addAll(bsdCellValues)
    return cellValuesPerLine
}

fun getHorizontalCellValues(cellValues: List<List<CellValue>>): List<CellValueLine> {
//    Log.d(aiTag, "horizontalCellValues")
    val horizontalCellValues = mutableListOf<CellValueLine>()
    for(row in cellValues){
//        Log.d(aiTag, "${row.map { it.id }}")
        horizontalCellValues.add(
            CellValueLine(
                cellValuesList = row,
                lineType = LineType.Horizontal
            )
        )
    }
    return horizontalCellValues
}

fun getVerticalCellValues(
    rowIndices: IntRange,
    columnIndices: IntRange,
    cellValues: List<List<CellValue>>
): List<CellValueLine> {
//    Log.d(aiTag, "verticalCellValues")
    val verticalCellValues = mutableListOf<CellValueLine>()
    for (col in columnIndices){
        val cellValuesPerLine = mutableListOf<CellValue>()
        for (row in rowIndices){
            cellValuesPerLine.add(cellValues[row][col])
        }
//        Log.d(aiTag, "${cellValuesPerLine.map { it.id }}")
        verticalCellValues.add(
            CellValueLine(
                cellValuesList = cellValuesPerLine,
                lineType = LineType.Vertical
            )
        )
    }
    return verticalCellValues
}

fun getFsdCellValues(
    rowIndices: IntRange,
    columnIndices: IntRange,
    cellValues: List<List<CellValue>>
): List<CellValueLine> {
//    Log.d(aiTag, "fsdCellValues")
    val fsdCellValues = mutableListOf<CellValueLine>()
    for (startCol in columnIndices){
        val cellValuesPerLine = mutableListOf<CellValue>()
        rowIndices.zip(startCol downTo columnIndices.first).forEach {
            cellValuesPerLine.add(cellValues[it.first][it.second])
        }
//        Log.d(aiTag, "${cellValuesPerLine.map { it.id }}")
        fsdCellValues.add(
            CellValueLine(
                cellValuesList = cellValuesPerLine,
                lineType = LineType.FSD
            )
        )
    }

    for(startRow in 1..rowIndices.last){
        val cellValuesPerLine = mutableListOf<CellValue>()
        (startRow..rowIndices.last).zip(columnIndices.reversed()).forEach{
            val cell = cellValues[it.first][it.second]
            cellValuesPerLine.add(cell)
        }
//        Log.d(aiTag, "${cellValuesPerLine.map { it.id }}")
        fsdCellValues.add(
            CellValueLine(
                cellValuesList = cellValuesPerLine,
                lineType = LineType.FSD
            )
        )
    }

    return fsdCellValues
}

fun getBsdCellValues(
    rowIndices: IntRange,
    columnIndices: IntRange,
    cellValues: List<List<CellValue>>
): List<CellValueLine> {
//    Log.d(aiTag, "bsdCellValues")
    val bsdCellValues = mutableListOf<CellValueLine>()
    for (startCol in columnIndices){
        val cellValuesPerLine = mutableListOf<CellValue>()
        (rowIndices).zip(startCol..columnIndices.last).forEach {
            cellValuesPerLine.add(cellValues[it.first][it.second])
        }
//        Log.d(aiTag, "${cellValuesPerLine.map { it.id }}")
        bsdCellValues.add(
            CellValueLine(
                cellValuesList = cellValuesPerLine,
                lineType = LineType.BSD
            )
        )
    }

    for (startRow in 1..rowIndices.last){
        val cellValuesPerLine = mutableListOf<CellValue>()
        (startRow..rowIndices.last).zip(columnIndices).forEach {
            cellValuesPerLine.add(cellValues[it.first][it.second])
        }
//        Log.d(aiTag, "${cellValuesPerLine.map { it.id }}")
        bsdCellValues.add(
            CellValueLine(
                cellValuesList = cellValuesPerLine,
                lineType = LineType.BSD
            )
        )
    }

    return bsdCellValues
}

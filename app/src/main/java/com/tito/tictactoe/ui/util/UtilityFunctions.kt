package com.tito.tictactoe.ui.util

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tito.tictactoe.R
import com.tito.tictactoe.ui.fragments.appTag
import com.tito.tictactoe.ui.util.UtilityData.CellValue


const val utilTag = "$appTag-util"
fun initializeCellValues(columnsPerRow: Int, rowsPerColumn: Int): List<List<CellValue>>{
    val result: MutableList<List<CellValue>> = mutableListOf()
    for(rowIndex in 0 until rowsPerColumn){
        val columnsOnRow = mutableListOf<CellValue>()
        for (columnIndex in 0 until columnsPerRow){
            val cellId = "$rowIndex$columnIndex"
            columnsOnRow.add(
                CellValue(
                    id = cellId,
                    rowIndex = rowIndex,
                    columnIndex = columnIndex
                )
            )
        }
        result.add(columnsOnRow)
    }
    return result
}

data class CheckConsecAvatars(
    var exists: Boolean = false,
    var startCell: CellValue? = null,
    var endCell: CellValue? = null
)
val noConsecAvatars = CheckConsecAvatars()
fun checkConsecAvatars(
    cellValues: List<List<CellValue>>,
    consecAvatarsCount: Int,
    targetAvatarDrawable: Int? = null
): CheckConsecAvatars{
    Log.d(utilTag, "check consecutive avatars called")
    val numRows = cellValues.size
    val numCols = cellValues[0].size
//    val isconsecutive avatarsVerticals = checkVerticals(
    val isConsecAvatarsVerticals = checkVerticals(
        cellValues = cellValues,
        numRows = numRows,
        numCols = numCols,
        consecAvatarsCount = consecAvatarsCount,
        targetAvatarDrawable = targetAvatarDrawable
    )
    
    val isConsecAvatarsHorizontals = checkHorizontals(
        cellValues = cellValues,
        numRows = numRows,
        numCols = numCols,
        consecAvatarsCount = consecAvatarsCount,
        targetAvatarDrawable = targetAvatarDrawable
    )
    val isConsecAvatarsFSD = checkForwardSlantingDiagonals(
        cellValues = cellValues,
        numRows = numRows,
        numCols = numCols,
        consecAvatarsCount = consecAvatarsCount,
        targetAvatarDrawable = targetAvatarDrawable
    )
    val isConsecAvatarsBSD = checkBackwardSlantingDiagonals(
        cellValues = cellValues,
        numRows = numRows,
        numCols = numCols,
        consecAvatarsCount = consecAvatarsCount,
        targetAvatarDrawable = targetAvatarDrawable
    )
    val allChecks = setOf(
        isConsecAvatarsVerticals,
        isConsecAvatarsHorizontals,
        isConsecAvatarsFSD,
        isConsecAvatarsBSD
    )
    for(checkConsecAvatars in allChecks){
        if(checkConsecAvatars.exists){
            return checkConsecAvatars
        }
    }
    return noConsecAvatars
}

fun checkVerticals(
    cellValues: List<List<CellValue>>,
    numRows: Int,
    numCols: Int,
    consecAvatarsCount: Int,
    targetAvatarDrawable: Int? = null
): CheckConsecAvatars{
    Log.d(utilTag, "checking verticals")
    //All rows should contain the same number of columns
    for(col in 0 until numCols){
        val cellValuesPerColumn: MutableList<CellValue> = mutableListOf()
        for(row in 0 until numRows){
            val cell = cellValues[row][col]
            cellValuesPerColumn.add(cell)
        }
        val checkConsecAvatars = isConsecAvatars(
            consecAvatarsCount,
            cellValuesPerColumn,
            targetAvatarDrawable
        )
        if(checkConsecAvatars.exists){
            Log.d(utilTag, "consecutive avatars emerged on verticals")
            return checkConsecAvatars
        }
    }
    return noConsecAvatars
}


fun checkHorizontals(
    cellValues: List<List<CellValue>>,
    numRows: Int,
    numCols: Int,
    consecAvatarsCount: Int,
    targetAvatarDrawable: Int? = null
): CheckConsecAvatars{
    Log.d(utilTag, "checking horizontals")
    //All rows should contain the same number of columns

    for (row in cellValues){
        val checkConsecAvatars = isConsecAvatars(
            consecAvatarsCount,
            row,
            targetAvatarDrawable
        )
        if(checkConsecAvatars.exists){
            Log.d(utilTag, "consecutive avatars emerged on horizontals")
            return checkConsecAvatars
        }
    }
    return noConsecAvatars
}

fun checkForwardSlantingDiagonals(
    cellValues: List<List<CellValue>>,
    numRows: Int, numCols: Int,
    consecAvatarsCount: Int,
    targetAvatarDrawable: Int? = null
): CheckConsecAvatars{
    Log.d(utilTag, "checking forward slanting diagonals")
    val msg = "consecutive avatars emerged on forward slanting diagonals"

    var rowRange = 0 until numRows
    //iterating through the columns on the first row and moving diagonally backwards
    for(col in 0 until numCols){
        //FSD - forward slanting diagonal
        val cellValuesPerFSD: MutableList<CellValue> = mutableListOf()
        val colRange = col downTo 0
        rowRange.zip(colRange).forEach{
            val cell = cellValues[it.first][it.second]
            cellValuesPerFSD.add(cell)
        }
        val checkConsecAvatars = isConsecAvatars(
            consecAvatarsCount,
            cellValuesPerFSD,
            targetAvatarDrawable
        )
        if(checkConsecAvatars.exists){
            Log.d(utilTag, msg)
            return checkConsecAvatars
        }
    }

    val colRange = (numCols - 1) downTo 0
    //iterating from the second row to the last and moving diagonally backwards
    for(row in 1 until numRows){
        val cellValuesPerFSD: MutableList<CellValue> = mutableListOf()
        rowRange = row until numRows
        rowRange.zip(colRange).forEach{
            val cell = cellValues[it.first][it.second]
            cellValuesPerFSD.add(cell)
        }
        val checkConsecAvatars = isConsecAvatars(
            consecAvatarsCount,
            cellValuesPerFSD,
            targetAvatarDrawable
        )
        if(checkConsecAvatars.exists){
            Log.d(utilTag, msg)
            return checkConsecAvatars
        }
    }
    return noConsecAvatars
}

fun checkBackwardSlantingDiagonals(
    cellValues: List<List<CellValue>>,
    numRows: Int, numCols: Int,
    consecAvatarsCount: Int,
    targetAvatarDrawable: Int? = null
): CheckConsecAvatars{
    Log.d(utilTag, "checking backward slanting diagonals")
    val msg = "consecutive avatars emerged on backward slanting diagonals"

    var rowRange = 0 until numRows
    //iterating from the columns on the first row and moving diagonally forward
    for(col in 0 until numCols){
        //BSD - backward slanting diagonal
        val cellValuesPerBSD: MutableList<CellValue> = mutableListOf()
        val colRange = col until numCols
        rowRange.zip(colRange).forEach {
            val cell = cellValues[it.first][it.second]
            cellValuesPerBSD.add(cell)
        }
        val checkConsecAvatars = isConsecAvatars(
            consecAvatarsCount,
            cellValuesPerBSD,
            targetAvatarDrawable
        )
        if(checkConsecAvatars.exists){
            Log.d(utilTag, msg)
            return checkConsecAvatars
        }
    }

    val colRange = 0 until numCols
    //iterating from second row downwards, starting at the first column on each row and moving diagonally forward
    for(row in 1 until numRows){
        //BSD - backward slanting diagonal
        val cellValuesPerBSD: MutableList<CellValue> = mutableListOf()

        rowRange = row until numRows
        rowRange.zip(colRange).forEach {
            val cell = cellValues[it.first][it.second]
            cellValuesPerBSD.add(cell)
        }
        val checkConsecAvatars = isConsecAvatars(
            consecAvatarsCount,
            cellValuesPerBSD,
            targetAvatarDrawable
        )
        if(checkConsecAvatars.exists){
            Log.d(utilTag, msg)
            return checkConsecAvatars
        }
    }
    return noConsecAvatars
}


fun isConsecAvatars(
    targetConsecCount: Int,
    cellValues: List<CellValue>,
    targetAvatarDrawable: Int? = null
): CheckConsecAvatars {
    if (targetConsecCount <= 1){
        Log.d(utilTag, "consecAvatarsCount=$targetConsecCount not greater than 1")
        return noConsecAvatars
    }

    if (cellValues.size < targetConsecCount){
        Log.d(utilTag, "numberOfCellValues=${cellValues.size} less than consecAvatarsCount=$targetConsecCount")
        return noConsecAvatars
    }

    var previousAvatar: Int?
    var currentAvatar: Int?
    var consecCount = 1

    for((index, cellValue) in cellValues.withIndex()) {
        currentAvatar = cellValue.avatarDrawableRes
        if (index == 0 || currentAvatar == null) {
            continue
        }
        previousAvatar = cellValues[index - 1].avatarDrawableRes

        if (targetAvatarDrawable == null){
            // looking for any consecutive avatar drawable
            if (currentAvatar == previousAvatar){
                consecCount++
            }else{
                consecCount = 1
            }
        }else{
            // looking for a specific avatar drawable
            if (currentAvatar == targetAvatarDrawable){
                if(currentAvatar == previousAvatar) {
                    consecCount++
                }
                else {
                    consecCount = 1
                }
            }else{
                continue
            }
        }

        if(consecCount == targetConsecCount){
            val firstCell = cellValues[index - targetConsecCount + 1]
            Log.d(utilTag, "consecutive cells from (${firstCell.id} - ${cellValue.id})")
            return CheckConsecAvatars(
                exists = true,
                startCell = firstCell,
                endCell = cellValue
            )
        }
    }



    return noConsecAvatars
}
//fun isConsecAvatars(
//    consecAvatarsCount: Int,
//    cellValues: List<CellValue>,
//    targetAvatarDrawable: Int? = null
//): CheckConsecAvatars {
//    var previousAvatar: Int?
//    var currentAvatar: Int?
//    var consecCount = 1
//
//    if (consecAvatarsCount <= 1){
//        throw Exception("consecAvatarsCount is $consecAvatarsCount, it should be greater than 1")
//    }
//
//    if (cellValues.size < consecAvatarsCount){
//        return noConsecAvatars
//    }
//    for((index, cellValue) in cellValues.withIndex()){
//        if(index == 0) {
//            continue
//        }
//        currentAvatar = cellValue.avatarDrawableRes
//        previousAvatar = cellValues[index - 1].avatarDrawableRes
//
//
//        if (targetAvatarDrawable == null){
//            Log.d(utilTag, "checking for any consecutiveAvatarDrawable")
//            if(currentAvatar != null && previousAvatar != null && currentAvatar == previousAvatar){
//                consecCount++
//            }else{
//                consecCount = 1
//            }
//        }else{
//            Log.d(utilTag, "checking for targetAvatar $targetAvatarDrawable")
//            if (currentAvatar == targetAvatarDrawable){
//                if(currentAvatar == previousAvatar){
//                    consecCount++
//                }else{
//                    consecCount = 1
//                }
//            }else{
//                continue
//            }
//        }
//
//        if(consecCount == consecAvatarsCount){
//            val firstCell = cellValues[index - consecAvatarsCount + 1]
//            Log.d(utilTag, "consecutive cells from (${firstCell.id} - ${cellValue.id})")
//            return CheckConsecAvatars(
//                consecAvatarsExists = true,
//                startCell = firstCell,
//                endCell = cellValue
//            )
//        }
//    }
//    return noConsecAvatars
//}

/**
 * returns the size for each cell based on the padding between each cell and the required row width
 * */
fun getCellSize(cellPaddingFraction:Double, columnsPerRow: Int, boardWidth: Double): Double{
    val totalCellPaddingFraction = 2 * cellPaddingFraction
    val cellSize = boardWidth/(totalCellPaddingFraction*(columnsPerRow + 1) + columnsPerRow)
    return cellSize
}


fun refreshFragment(
    fragmentContainerId: Int,
    fragmentManager: FragmentManager,
    fragment: Fragment
){
    Log.d(appTag, "refresh fragment called")
    // TODO this returns the fragment inside the container, not sure what happens if there's more than one
    val oldInstance = fragmentManager.findFragmentById(fragmentContainerId)!!
    Log.d(appTag, "fragment found = $oldInstance")
    val ft = fragmentManager.beginTransaction()
    ft.remove(oldInstance)
    ft.add(R.id.fragmentContainerView, fragment)
//    ft.detach(fragment)
//    ft.attach(PlayFragment())
    ft.commit()
    Log.d(appTag, "refresh fragment executed successfully")
}

/*

fun getShadowCoordinates(
    firstCellCoordinates: Offset,
    lastCellCoordinates: Offset,
    elevation: Float
): List<Offset>{
    var x1 = firstCellCoordinates.x
    var x2 = lastCellCoordinates.x
    val y1 = firstCellCoordinates.y + elevation
    val y2 = lastCellCoordinates.y + elevation

    if(firstCellCoordinates.y == lastCellCoordinates.y || firstCellCoordinates.x == lastCellCoordinates.x) {
        x1 = firstCellCoordinates.x + elevation
        x2 = lastCellCoordinates.x + elevation
    }

    return listOf(Offset(x1, y1), Offset(x2, y2))
}



// Verticals
fun getVerticalStartX(
    lineNumber: Int,
    cellValues: List<List<CellValue>>,
    origin: Offset = Offset.Zero
): Float{
    val firstRow = 0
    val startX = cellValues.let {
        (it[firstRow][lineNumber-1].coordinates.value.topRight.x +
                it[firstRow][lineNumber].coordinates.value.topLeft.x)/2
    }
    return startX
}

fun getVerticalStartY(
    lineNumber: Int,
    cellValues: List<List<CellValue>>,
    origin: Offset = Offset.Zero
): Float{
    val firstRow = 0
    val startY = cellValues.let {
        (it[firstRow][lineNumber-1].coordinates.value.topRight.y +
                it[firstRow][lineNumber].coordinates.value.topLeft.y)/2
    } - origin.y
    return startY
}

fun getVerticalEndY(
    cellValues: List<List<CellValue>>,
    origin: Offset = Offset.Zero
): Float{
    val lastRow = cellValues.lastIndex
    val result = cellValues.let {
        it[lastRow][0].coordinates.value.bottom
    } - origin.y
    return result
}


// Horizontals
fun getHorizontalStartX(
    lineNumber: Int,
    cellValues: List<List<CellValue>>,
    origin: Offset = Offset.Zero
): Float{
    val firstColumn = 0
    val startX = cellValues.let {
        (it[lineNumber - 1][firstColumn].coordinates.value.bottomLeft.x +
                it[lineNumber][firstColumn].coordinates.value.topLeft.x)/2
    }
    return startX  - origin.x
}

fun getHorizontalEndX(
    cellValues: List<List<CellValue>>,
    origin: Offset = Offset.Zero
): Float{
    val lastColumn = cellValues[0].lastIndex
    val endX = cellValues.let {
        it[0][lastColumn].coordinates.value.right
    } - origin.x
    return endX
}

fun getHorizontalStartY(
    lineNumber: Int,
    cellValues: List<List<CellValue>>,
    origin: Offset = Offset.Zero
): Float{
    val firstColumn = 0
    val startY = cellValues.let {
        (it[lineNumber - 1][firstColumn].coordinates.value.bottomLeft.y +
                it[lineNumber][firstColumn].coordinates.value.topLeft.y)/2
    } - origin.y
    return startY
}
*/

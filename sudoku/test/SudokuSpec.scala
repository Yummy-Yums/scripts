import munit.*
import cats.syntax.all.*

class SudokuSpec extends FunSuite {

  // format off
  val maybeSudoku = Sudoku.from(
    "435269781" +
    "682571493" +
    "197834562" +
    "826195347" +
    "374682915" +
    "951743628" +
    "519326874" +
    "248957136" +
    "763418259"
  )

  // format on
  
  val sudokuF: FunFixture[Sudoku] = FunFixture(_ => maybeSudoku.fold(failSuite(_), identity), _ => ())

  sudokuF.test("Sudoku.get(x,y) should extract the number at (x,y) (0 based, from top left)") { sudoku =>
    assertEquals(sudoku.get(0)(0), 4)
    assertEquals(sudoku.get(1)(0), 3)
    assertEquals(sudoku.get(2)(7), 8)
  }

  sudokuF.test("Sudoku.getRow(n) should extract nth row from top") { sudoku =>
    assertEquals(sudoku.getRow(0), Vector(4, 3, 5, 2, 6, 9, 7, 8, 1))
    assertEquals(sudoku.getRow(6), Vector(5, 1, 9, 3, 2, 6, 8, 7, 4))
  }

  sudokuF.test("Sudoku.getColumn(n) should extract nth column from left") { sudoku =>
    assertEquals(sudoku.getColumn(0), Vector(4, 6, 1, 8, 3, 9, 5, 2, 7))
    assertEquals(sudoku.getColumn(6), Vector(7, 4, 5, 3, 9, 6, 8, 1, 2))
  }

  sudokuF.test("Sudoku.getCellOf(n) should extract the correct cell") { sudoku =>
    assert(sudoku.getCellOf(1)(1).forall((1 to 9).contains))
    assert(sudoku.getCellOf(7)(3).forall((1 to 9).contains))
  } 
}  

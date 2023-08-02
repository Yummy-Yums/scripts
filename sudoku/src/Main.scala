import com.monovore.decline.*
import cats.syntax.all.*
import Sudoku.*
import cats.conversions.all.autoConvertProfunctorVariance

val sudokuArgument: Opts[Sudoku] =
  Opts.argument[String]("sudoku").mapValidated(Sudoku.from(_).toValidatedNel)

object Main extends CommandApp(
  name = "sudokuSolver",
  header = "Solves sudokus passed as 81 chars string with . or 0 in place of empty cells",
  main = sudokuArgument.map(sudoku =>
    val s = solve(sudoku)
    s.headOption.fold { // We will print the first solution only
      System.err.println("Sudoku not solvable"); System.exit(1)
    } {
      s => System.out.println(s.asPrettyString); System.exit(0)
    }
  )
)

import cats.syntax.all.*

final case class Sudoku private(data: Vector[Int]){

  def get(x: Int)(y: Int): Int = data(y * 9 + x)

  def getRow(y: Int): Vector[Int] = data.slice(y * 9, (y + 1) * 9)

  def getColumn(x: Int): Vector[Int] = (0 until 9).toVector.map(get(x))

  def getCellOf(x: Int)(y: Int): Vector[Int] = {
    def span(n: Int): Vector[Int] = {
      val x: Int = (3 * (n / 3))
      Vector(x, x + 1, x + 2)
    }
  
    for {
      b <- span(y)
      a <- span(x)
    } yield get(a)(b)
  }

  def getZero: Option[(Int, Int)] = Option(data.indexWhere(_ === 0))
    .filterNot(_ === -1)
    .map(i => (i % 9, i / 9))
  

  def fitsInPlace(x: Int, y: Int)(value: Int): Boolean = 
    !(getCellOf(x)(y).contains(value) || getRow(y).contains(value) || getColumn(x).contains(value))

  def set(x: Int, y: Int)(value: Int): Sudoku = Sudoku(
    data.updated(y * 9 + x, value)
  )

  def asPrettyString: String = {
    def showRow(a: Vector[Int]): String =
      a.grouped(3).map(_.mkString).mkString("│")

    (0 until 9)
      .map(x => getRow(x))
      .map(showRow)
      .grouped(3)
      .map(_.mkString("\n"))
      .mkString("\n───┼───┼───\n")
  }


}

object Sudoku {
 
  def from(s: String): Either[String, Sudoku] = 
    s.replace('.', '0')
      .asRight[String]
      .ensure("The sudoku string doesn't contain only digits")(
        _.forall(_.isDigit)
      )
      .map(_.toVector.map(_.asDigit))
      .ensure("The sudoku string is not exactly 81 characters long")(
        _.length === 81
      )
      .map(Sudoku.apply)

  def solve(s: Sudoku): List[Sudoku] = s.getZero.fold(s :: Nil) {
    case (x, y) => calcStep(x, y)(s) >>= solve
  }

  def calcStep(x: Int, y: Int)(s: Sudoku): List[Sudoku] = 1
    .to(9)
    .filter(s.fitsInPlace(x, y))
    .map(s.set(x, y))
    .toList

}

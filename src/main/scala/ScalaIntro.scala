class ScalaIntro {

}

case class Student(name: String, grade: Int, id: String)

object ScalaIntro {
  def main(args: Array[String]): Unit = {
    val dennis = new Student("Dennis", 76, "0846356")
    for (i <- 1 to 10) println(i)
    val t = foo(6.7, List("hi", "there"))(8)
    sum(7.8, 6, 4)
    val numbers = List(8.3, 7.6, 2.3)
    sum(numbers:_*)
    println(applyTwice(5, x => x*3))
    var a = 5
    println(makeTriple({a += 1; println(a); a}))
    var i = 0
    myWhile(i < 10) {
      println(i)
      i += 1
    }
  }

  def applyTwice(e: Int, f: Int => Int): Int = f(f(e))

  def makeTriple(arg: => Int): (Int, Int, Int) = (arg, arg, arg)

  def myWhile(cond: => Boolean)(body: => Unit): Unit = if (cond) {
    body
    myWhile(cond)(body)
  }

  def square(x: Double) = x*x

  def sum(nums: Double*): Double = nums.sum

  def foo(x: Double, names: List[String] = Nil)(implicit y: Int = 9): Double = {
    val first = if(names.nonEmpty) names.head else "No one"
    val lens = for {
      name <- names 
      first = name(0)
      if first == 'A'
    } yield name.length
    val varWithLogic = {
      // logic goes here
      42
    }
    val i = 7
    i.toString()
    // i = null
    val lst = List(1,2,3)
    val lst2 = lst.map(x => x+1)
    val lst3 = lst.iterator.map(x => x+1).toSeq
    x * 2
  }
}

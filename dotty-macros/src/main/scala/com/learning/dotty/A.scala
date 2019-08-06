package p

type Labelled[T] = (String, T)

val a: Labelled[Int] = ("count", 1)
def b = a._2
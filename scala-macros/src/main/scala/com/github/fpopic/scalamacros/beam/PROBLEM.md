For the input:

```scala
import com.github.fpopic.scalamacros.beam.DefMacroCoder
import com.github.fpopic.scalamacros.beam.DefMacroCoder.{intCoder, stringCoder, listCoder}

val coder: Coder[Pojo] = DefMacroCoder.productCoder[Pojo]
```

The generated output should be:

```scala
import com.github.fpopic.scalamacros.Pojo
import org.apache.beam.sdk.coders.Coder
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util

new Coder[Pojo] {

  import com.github.fpopic.scalamacros.beam.DefMacroCoder.{
    intCoder,
    stringCoder,
    listCoder
  }

  override def encode(value: Pojo, os: OutputStream): Unit = {
    stringCoder.encode(value.s, os)
    intCoder.encode(value.i, os)
    listCoder(intCoder).encode(value.l, os)
  }

  override def decode(is: InputStream): Pojo = {
    Pojo(
      s = stringCoder.decode(is),
      i = intCoder.decode(is),
      l = listCoder(intCoder).decode(is)
    )
  }

  override def getCoderArguments: util.List[_ <: Coder[_]] = {
    Collections.emptyList()
  }

  override def verifyDeterministic(): Unit =  ()
}

```

import java.net.ServerSocket
import java.net.Socket
import java.io.PrintWriter
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import scala.concurrent.Future

val verbRegex = """GET (\S+) .*""".r
val userPathRegex = """/~(\w+)/(.*)""".r

@main def socket() = {
  implicit val ec = scala.concurrent.ExecutionContext.global
  val ss = new ServerSocket(4000)
  while(true) {
    val sock = ss.accept()
    Future(handleSocket(sock))
  }
}

def handleSocket(sock: Socket) = {
  val is = new BufferedReader(new InputStreamReader(sock.getInputStream()))
  val os = new PrintWriter(sock.getOutputStream())

  var firstLine = is.readLine()
  println(firstLine)
  var line = "not blank"
  while(line.nonEmpty) {
    line = is.readLine()
  }
  val optm = verbRegex.findFirstMatchIn(firstLine)
  optm match {
    case Some(m) =>
      val path = m.group(1)
      println(s"Path: $path")
      if (path.startsWith("/greet/")) {
        greet(os, path.drop(7))
      } else {
        serveFile(os, path)
      }
      os.flush()
    case None => println("Not a GET")
  }
  sock.close()
}

def greet(pw: PrintWriter, name: String) = {
  pw.println(
    """HTTP/1.1 200 OK

    <html>
    <head><title>Greeting</title></head>
    <body>
      <h1>Web Apps!</h1>
      <p>Welcome, ???!</p>
      </body>
    </html>
    """.replace("???", name)
  )
}

def serveFile(pw: PrintWriter, path: String) = {

  val fileName = {
    val mopt = userPathRegex.findFirstMatchIn(path)
    val p1 = mopt match {
      case Some(m) => s"/users/${m.group(1)}/Local/HTML-Documents/${m.group(2)}"
      case None => s"public/$path"
    }
    if (p1.endsWith("/")) s"${p1}index.html" else p1
  }
  println(s"filename = $fileName")
  val file = java.io.File(fileName)
  if (file.exists) {
    val source = io.Source.fromFile(file)
    val fileContents = source.mkString
    source.close()
    pw.println("HTTP/1.1 200 OK")
    pw.println("")
    pw.println(fileContents)
  } else {
    pw.println("HTTP/1.1 404 Not Found")
    pw.println("")
  }
}
package APIs.StudentAPI

case class StudentLoginMessage(userName:String, password:String, email:String, number:String) extends StudentMessage[String]

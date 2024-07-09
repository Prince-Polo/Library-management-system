package APIs.StudentAPI

case class StudentQueryMessage(doctorName:String, patientName:String) extends StudentMessage[String]

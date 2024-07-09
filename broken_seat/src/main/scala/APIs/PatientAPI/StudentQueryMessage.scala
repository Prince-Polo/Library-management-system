package APIs.PatientAPI

case class StudentQueryMessage(doctorName:String, patientName:String) extends StudentMessage[String]

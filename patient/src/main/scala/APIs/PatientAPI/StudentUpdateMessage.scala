package APIs.PatientAPI

case class StudentUpdateMessage(userName: String, password: String, email: String, number: String, newPassword: Option[String] = None, newEmail: Option[String] = None) extends StudentMessage[Int]
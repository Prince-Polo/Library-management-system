package APIs.PatientAPI

case class StudentRegisterMessage(userName:String, password:String, email:String, number:String) extends StudentMessage[Int]

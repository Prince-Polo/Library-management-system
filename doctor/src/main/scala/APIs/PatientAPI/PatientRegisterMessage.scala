package APIs.PatientAPI

case class PatientRegisterMessage(userName:String,password:String,email:String,number:String) extends PatientMessage[Int]

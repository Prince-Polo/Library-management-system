package APIs.PatientAPI

case class PatientLoginMessage(userName:String, password:String, email:String, number:String) extends PatientMessage[String]

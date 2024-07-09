package APIs.PatientAPI

case class StudentLoginResponse(valid: Boolean, id: Option[Int] = None, authority: Option[Int] = None)

package APIs.StudentAPI

case class StudentLoginResponse(valid: Boolean, id: Option[Int] = None, authority: Option[Int] = None)

package APIs.AdminAPI

case class AddJobMessage(doctorName:String, patientName:String) extends AdminMessage[String]

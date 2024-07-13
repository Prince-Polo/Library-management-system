package APIs.AdminAPI

case class AdminLoginMessage(AdminName:String, AdminPassword:String) extends AdminMessage[String]

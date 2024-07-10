package APIs.AdminAPI


case class AdminRegisterMessage(AdminName:String,AdminPassword:String,AdminId:String) extends AdminMessage[Int]

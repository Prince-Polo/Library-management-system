package APIs.JobAPI

import Common.API.API
import Global.ServiceCenter.jobServiceCode
import io.circe.Decoder

abstract class JobMessage[ReturnType:Decoder] extends API[ReturnType](jobServiceCode)
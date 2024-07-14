package APIs.SeatAPI

import Common.API.API
import Global.ServiceCenter.seatServiceCode
import io.circe.Decoder

abstract class SeatMessage[ReturnType: Decoder] extends API[ReturnType](seatServiceCode)

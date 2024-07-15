package APIs.JobAPI

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class ApproveJobMessage(
                              jobId: BigInt,
                              jobStudentId: Array[Array[String]], // 二维 TEXT 数组
                              jobShortDescription: String,
                              jobLongDescription: String,
                              jobHardness: Int, // 更改为 Int
                              jobCredit: Int, // 更改为 Int
                              jobComplete: Boolean,
                              jobBooked: Boolean,
                              jobApproved: Boolean,
                              jobCurrent: Int, // 新增
                              jobRequired: Int // 新增
                            )

object ApproveJobMessage {
  implicit val encoder: Encoder[ApproveJobMessage] = deriveEncoder[ApproveJobMessage]
  implicit val decoder: Decoder[ApproveJobMessage] = deriveDecoder[ApproveJobMessage]
}


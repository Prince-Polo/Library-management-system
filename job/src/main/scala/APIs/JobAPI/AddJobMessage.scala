package APIs.JobAPI

case class AddJobMessage(
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

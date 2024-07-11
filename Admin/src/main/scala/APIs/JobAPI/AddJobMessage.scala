package APIs.JobAPI

case class AddJobMessage(
                          jobId: BigInt,
                          jobStudentId: String,
                          jobShortDescription: String,
                          jobLongDescription: String,
                          jobHardness: BigInt,
                          jobCredit: BigInt,
                          jobComplete: Boolean,//学生是否确认完成
                          jobBooked: Boolean,//学生是否预约
                          jobApproved: Boolean//教师是否确认完成
                        )

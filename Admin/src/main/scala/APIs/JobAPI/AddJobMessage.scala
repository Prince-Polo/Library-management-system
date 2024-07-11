package APIs.JobAPI

case class AddJobMessage(
                          jobId: BigInt,
                          jobStudentId: String,
                          jobShortDescription: String,
                          jobLongDescription: String,
                          jobHardness: BigInt,
                          jobCredit: BigInt,
                          jobComplete: Boolean,
                          jobBooked: Boolean,
                          jobApproved: Boolean
                        )

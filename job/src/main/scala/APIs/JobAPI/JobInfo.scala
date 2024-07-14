package APIs.JobAPI

case class JobInfo(
                    jobId: Int,
                    jobShortDescription: String,
                    jobLongDescription: String,
                    jobHardness: Int,
                    jobCredit: Int,
                    jobCurrent: Int,
                    jobRequired: Int,
                    jobEnrolled:Int
                  )


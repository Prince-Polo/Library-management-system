export type Job={
    jobId: number;
    jobShortDescription: string;
    jobLongDescription: string;
    jobHardness: number;
    jobCredit: number;
    jobCurrent: number;
    jobEnrolled: number;
    jobRequired: number;
}
export type Task={
    jobId: number;
    status: number;
}

import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class ForceEndTaskMessage extends JobMessage{
    jobId: number;
    studentId: string;
    constructor(jobId: number, studentId:string) {
        super();
        this.jobId = jobId;
        this.studentId=studentId;
    }
}

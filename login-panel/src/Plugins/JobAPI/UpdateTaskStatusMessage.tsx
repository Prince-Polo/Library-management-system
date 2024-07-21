import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class UpdateTaskStatusMessage extends JobMessage{
    jobId: number;
    studentId: string;
    status: number;
    constructor(jobId: number, studentId:string, status: number) {
        super();
        this.jobId = jobId;
        this.studentId=studentId;
        this.status = status;
    }
}

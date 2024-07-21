import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class JobStudentsQueryMessage extends JobMessage{
    jobId: number;
    constructor(jobId: number) {
        super();
        this.jobId = jobId;
    }
}

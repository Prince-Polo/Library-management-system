import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class UpdateJobCurrentMessage extends JobMessage{
      jobId: number;
      increment: number;
    constructor(jobId: number, increment: number) {
        super();
        this.jobId = jobId;
        this.increment = increment;
    }
}

import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class DeleteJobMessage extends JobMessage{
    jobId:number;
    constructor(jobId:number) {
        super();
        this.jobId=jobId;
    }
}
import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class AcceptJobMessage extends StudentMessage {
    token: string;
    jobId: number;
    constructor( token: string, jobId: number) {
        super();
        this.token=token;
        this.jobId=jobId;
    }
}
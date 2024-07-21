import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'


export class SubmitJobMessage extends StudentMessage {
    jobId: number;
    token:string;
    constructor( jobId: number,token:string ) {
        super();
        this.jobId=jobId;
        this.token=token;
    }
}
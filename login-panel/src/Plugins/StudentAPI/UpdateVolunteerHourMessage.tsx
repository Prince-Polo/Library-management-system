import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class UpdateVolunteerHourMessage extends StudentMessage{
    jobId: number;
    studentId:string;
    constructor(jobId: number, studentId:string) {
        super();
        this.jobId = jobId;
        this.studentId=studentId;
    }
}

import { JobMessage } from 'Plugins/JobAPI/JobMessage'

export class CheckStudentTaskStatusMessage extends JobMessage{
    studentId: string;
    constructor(studentId: string){
        super();
        this.studentId=studentId;
    }
}
import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class UpdateViolationCountMessage extends StudentMessage {
    studentNumber: string;
    violationCount: string;
    constructor(studentNumber: string,violationCount: string) {
        super();
        this.violationCount = violationCount;
        this.studentNumber=studentNumber;
    }
}

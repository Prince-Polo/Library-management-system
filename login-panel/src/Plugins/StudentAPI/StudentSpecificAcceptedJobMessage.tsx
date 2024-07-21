import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentSpecificAcceptedJobMessage extends StudentMessage {
    token: string
    constructor(token: string) {
        super();
        this.token=token;
    }
}
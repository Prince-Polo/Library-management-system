import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentInfoUsingTokenMessage extends StudentMessage {
    token: string

    constructor(token: string) {
        super();
        this.token=token;
    }
}
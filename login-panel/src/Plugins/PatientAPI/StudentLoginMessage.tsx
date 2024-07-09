import { StudentMessage } from 'Plugins/PatientAPI/StudentMessage'
import {Info} from 'Plugins/CommonUtils/Info'

export class StudentLoginMessage extends StudentMessage {
    info: Info;

    constructor(info: Info) {
        super();
        this.info=info;
    }
}

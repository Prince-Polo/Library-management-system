import { Info} from 'Plugins/CommonUtils/Info'
import { StudentMessage } from 'Plugins/PatientAPI/StudentMessage'
export class StudentRegisterMessage extends StudentMessage {
    info: Info;
    constructor(info: Info) {
        super()
        this.info = info
    }
}
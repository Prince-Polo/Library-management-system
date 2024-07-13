import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentUnregisterMessage extends StudentMessage {
    number: string;

    constructor( number: string) {
        super();
        this.number = number;
    }
}
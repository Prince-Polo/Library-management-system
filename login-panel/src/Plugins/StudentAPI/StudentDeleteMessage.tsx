import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentDeleteMessage extends StudentMessage {
    number: string;
    password: string;

    constructor( number: string, password: string )  {
        super();
        this.number = number;
        this.password = password;
    }
}
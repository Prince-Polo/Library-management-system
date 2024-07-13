import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentLoginMessage extends StudentMessage {
    userName: string;
    password:string;
    number: string;

    constructor( userName: string,
    password:string,
    number: string) {
        super();
        this.userName = userName;
        this.number = number;
        this.password = password;
    }
}

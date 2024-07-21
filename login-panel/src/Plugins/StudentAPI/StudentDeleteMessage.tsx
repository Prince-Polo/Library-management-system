import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentDeleteMessage extends StudentMessage {
    token: string;
    password: string;

    constructor( token: string, password: string )  {
        super();
        this.token = token;
        this.password = password;
    }
}
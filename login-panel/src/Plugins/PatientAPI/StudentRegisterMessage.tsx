import { StudentMessage } from 'Plugins/PatientAPI/StudentMessage'

export class StudentRegisterMessage extends StudentMessage {
    userName: string;
    password: string;
    email: string;
    number: string;

    constructor(userName: string, password: string, email:string,number:string) {
        super();
        this.userName = userName;
        this.password = password;
        this.email=email;
        this.number=number;
    }
}

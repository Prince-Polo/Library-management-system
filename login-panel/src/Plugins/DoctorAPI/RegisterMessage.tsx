import { DoctorMessage } from 'Plugins/DoctorAPI/DoctorMessage'

export class RegisterMessage extends DoctorMessage {
    userName: string;
    password: string;
    email:string;
    number:string;

    constructor(userName: string, password: string,email:string,number:string) {
        super();
        this.userName = userName;
        this.password = password;
        this.email=email;
        this.number=number;
    }
}
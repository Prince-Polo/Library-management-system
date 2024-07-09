import { PatientMessage } from 'Plugins/PatientAPI/PatientMessage'

export class PatientDeleteMessage extends PatientMessage {
    userName: string;
    password: string;
    email: string;
    number: string;

    constructor(userName: string, password: string, email: string, number: string) {
        super();
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.number = number;
    }
}
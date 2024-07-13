import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'


export class StudentUpdateMessage extends StudentMessage {
    userName: string;
    password:string;
    number: string;
    newPassword: string;
    constructor( userName: string, password:string, number: string,newPassword: string) {
        super();
        this.userName = userName;
        this.number = number;
        this.password = password;
        this.newPassword=newPassword
    }
}
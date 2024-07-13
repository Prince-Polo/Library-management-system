import { AdminMessage } from 'Plugins/AdminAPI/AdminMessage'

export class AdminLoginMessage extends AdminMessage {
    AdminName: string;
    AdminPassword: string;
//    email:string;
//    id:string;

    constructor(AdminName: string, AdminPassword: string){
        super();
        this.AdminName = AdminName;
        this.AdminPassword = AdminPassword;
//        this.email=email;
//        this.id=id;
    }
}
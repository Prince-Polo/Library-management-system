import { AdminMessage } from 'Plugins/AdminAPI/AdminMessage'

export class AdminRegisterMessage extends AdminMessage {
    AdminName: string;
    AdminPassword: string;
//    AdminEmail:string;
    AdminId:string;

    constructor(AdminName: string, AdminPassword: string,AdminId:string) {
        super();
    //    this.AdminEmail = AdminEmail;
        this.AdminPassword = AdminPassword;
        this.AdminId=AdminId;
        this.AdminName=AdminName;
    }
}
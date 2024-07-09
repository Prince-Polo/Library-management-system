// Define the message classes with toJson methods
import { stringify } from 'jsonfile/utils'

export abstract class API {
    serviceName:string
    public readonly type = this.getName()
    public getURL():string {
        return "http://127.0.0.1:10004/api/"+this.serviceName+"/"+this.type
    }

    private getName() {
        return this.constructor.name
    }
}

export class StudentInfoMessage extends API {
    constructor(public number: string) {
        super();
        this.serviceName = "studentService";
    }
}
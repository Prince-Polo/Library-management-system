import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class VolunteerStatusTrueMessage extends StudentMessage {
    number:string;
    constructor( number:string ) {
        super();
        this.number=number;
    }
}
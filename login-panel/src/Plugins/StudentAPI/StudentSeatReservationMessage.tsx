import { StudentMessage } from 'Plugins/StudentAPI/StudentMessage'

export class StudentSeatReservationMessage extends StudentMessage {
    token:string;
    floor: string;
    section: string;
    seatNumber: string;
    constructor(token:string,
                 floor: string,
                 section:string,
                seatNumber: string
                 ) {
        super();
        this.floor=floor;
        this.section=section;
        this.token=token;
        this.seatNumber=seatNumber;
    }
}
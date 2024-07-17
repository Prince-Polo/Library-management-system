import { SeatMessage } from 'Plugins/SeatAPI/SeatMessage'

export class SeatReservationMessage extends SeatMessage {
    floor: string;
    section: string;
    seatNumber: string;
    studentNumber:string;
    constructor( floor: string,
                 section:string,
                 seatNumber: string,
                 studentNumber:string,) {
        super();
        this.floor=floor;
        this.section=section;
        this.seatNumber=seatNumber;
        this.studentNumber=studentNumber;
    }
}
